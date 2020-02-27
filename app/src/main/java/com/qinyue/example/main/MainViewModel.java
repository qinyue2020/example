package com.qinyue.example.main;

import android.app.Application;

import com.qinyue.example.base.MyBaseViewModel;

import androidx.annotation.NonNull;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/26
 * 描述:主页的viewmodel
 **/
public class MainViewModel extends MyBaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
