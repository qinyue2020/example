package com.qinyue.example.splash;

import android.app.Activity;

import com.qinyue.example.login.LoginActivity;
import com.qinyue.example.main.MainActivity;
import com.qinyue.example.data.DataProvider;
import com.xuexiang.xui.widget.activity.BaseGuideActivity;

import java.util.List;


/**
 * 创建人:qinyue
 * 创建日期:2020/2/26
 * 描述:引导页
 **/
public class GuideActivity extends BaseGuideActivity {

    @Override
    protected List<Object> getGuideResourceList() {
        return DataProvider.getUsertGuides();
    }

    @Override
    protected Class<? extends Activity> getSkipClass() {
        return LoginActivity.class;
    }
}
