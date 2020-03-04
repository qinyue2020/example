package com.qinyue.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.qinyue.example.BR;
import com.qinyue.example.R;
import com.qinyue.example.base.AppViewModelFactory;
import com.qinyue.example.base.MyBaseActivity;
import com.qinyue.example.data.DataRepository;
import com.qinyue.example.databinding.ActivityLoginBinding;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.KeyboardUtils;

import java.util.Map;

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
        viewModel.uc.qqLoginEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //pSwitchObservable是boolean类型的观察者
                UMShareAPI umShareAPI = UMShareAPI.get(getApplication());
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener(){
                    /**
                     * @desc 授权开始的回调
                     * @param platform 平台名称
                     */
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                    }

                    /**
                     * @desc 授权成功的回调
                     * @param platform 平台名称
                     * @param action 行为序号，开发者用不上
                     * @param data 用户资料返回
                     */
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        ToastUtils.showShort("成功");
                    }
                    /**
                     * @desc 授权失败的回调
                     * @param platform 平台名称
                     * @param action 行为序号，开发者用不上
                     * @param t 错误原因
                     */
                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        ToastUtils.showShort("失败");                }
                    /**
                     * @desc 授权取消的回调
                     * @param platform 平台名称
                     * @param action 行为序号，开发者用不上
                     */
                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        ToastUtils.showShort("取消");
                    }
                });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        KeyboardUtils.removeAllKeyboardToggleListeners();
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
    public void getData(){

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
}
