package com.qinyue.example.base;

import com.qinyue.example.main.MainActivity;
import com.qinyue.example.R;
import com.xuexiang.xui.XUI;

import androidx.multidex.MultiDexApplication;
import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/26
 * 描述:
 **/
public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化mvvm
        LnitializeMvvm();
        //初始化xui
        LnitializeXUI();
    }

    /**
     * 初始化mvvm
     */
    private void LnitializeMvvm(){
        //初始化mvvm
        BaseApplication.setApplication(this);
        //是否开启日志打印
        KLog.init(true);
        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    /**
     * 初始化XUI
     */
    private void LnitializeXUI(){
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
    }
}
