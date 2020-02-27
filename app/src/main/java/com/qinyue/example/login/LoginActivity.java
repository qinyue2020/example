package com.qinyue.example.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.qinyue.example.BR;
import com.qinyue.example.R;
import com.qinyue.example.base.AppViewModelFactory;
import com.qinyue.example.base.MyBaseActivity;
import com.qinyue.example.data.DataRepository;
import com.qinyue.example.databinding.ActivityLoginBinding;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.KeyboardUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/27
 * 描述:登录页
 **/
public class LoginActivity extends MyBaseActivity<ActivityLoginBinding,LoginViewModel> {
    private CountDownButtonHelper mCountDownHelper;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }
    @Override
    public String initTitleText() {
        return null;
    }

    @Override
    public void initData() {
        super.initData();
        setTitleBarVisibility(false);
        mCountDownHelper = new CountDownButtonHelper(binding.btnGetVerifyCode, 60);
        binding.btnLogin.setEnabled(false);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        viewModel.uc.pVerifyEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //pSwitchObservable是boolean类型的观察者
                if (viewModel.uc.pVerifyEvent.getValue()!=null&&viewModel.uc.pVerifyEvent.getValue()) {
                    if (binding.etPhoneNumber.validate()) {
                        getVerifyCode(binding.etPhoneNumber.getEditValue());
                    }
                }
            }
        });
        binding.etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etPhoneNumber.validate()&&binding.etVerifyCode.validate()){
                    binding.btnLogin.setEnabled(true);
                }else{
                    if (binding.btnLogin.isEnabled()){
                        binding.btnLogin.setEnabled(false);
                    }
                }
            }
        });
        binding.etVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etPhoneNumber.validate()&&binding.etVerifyCode.validate()){
                    binding.btnLogin.setEnabled(true);
                }else{
                    if (binding.btnLogin.isEnabled()){
                        binding.btnLogin.setEnabled(false);
                    }
                }
            }
        });
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible){
                    if (binding.logimg.getVisibility()== View.VISIBLE){
                        binding.logimg.setVisibility(View.GONE);
                    }
                }else {
                    if (binding.logimg.getVisibility()!= View.VISIBLE){
                        binding.logimg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phoneNumber) {
        // TODO: 2019-11-18 这里只是界面演示而已
        mCountDownHelper.start();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        KeyboardUtils.removeAllKeyboardToggleListeners();
        super.onDestroy();
    }
    public void getData(){

    }
}
