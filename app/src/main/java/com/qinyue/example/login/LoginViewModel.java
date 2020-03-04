package com.qinyue.example.login;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.qinyue.example.base.MyBaseViewModel;
import com.qinyue.example.data.DataRepository;
import com.qinyue.example.main.MainActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/27
 * 描述:
 **/
public class LoginViewModel extends MyBaseViewModel<DataRepository> {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pVerifyEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> qqLoginEvent = new SingleLiveEvent<>();
    }
    public LoginViewModel(@NonNull Application application, DataRepository mRepository) {
        super(application);
        model = mRepository;
        //从本地取得数据绑定到View层
        userName.set(model.getUserName()==null?"":model.getUserName());
        password.set(model.getPassword()==null?"":model.getPassword());
    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });
    //获取验证码的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand getVerifyCodeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pVerifyEvent.setValue(uc.pVerifyEvent.getValue() == null || !uc.pVerifyEvent.getValue());
        }
    });
    //qq登录
    public BindingCommand qqLoginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.qqLoginEvent.setValue(uc.qqLoginEvent.getValue() == null || !uc.qqLoginEvent.getValue());
        }
    });
    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入验证码！");
            return;
        }
        //RaJava模拟登录
        addSubscribe(model.login()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismissDialog();
                        //保存账号密码
                        model.saveUserName(userName.get());
                        model.savePassword(password.get());
                        //进入DemoActivity页面
                        startActivity(MainActivity.class);
                        //关闭页面
                        finish();
                    }
                }));
    }

}
