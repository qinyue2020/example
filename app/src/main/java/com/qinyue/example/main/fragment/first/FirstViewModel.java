package com.qinyue.example.main.fragment.first;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.qinyue.example.LoadBugClass;

import androidx.annotation.NonNull;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/27
 * 描述:
 **/
public class FirstViewModel extends BaseViewModel {
    public FirstViewModel(@NonNull Application application) {
        super(application);
    }
    //获取验证码的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand getBugOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort(LoadBugClass.getBugString());
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
        }
    });
}
