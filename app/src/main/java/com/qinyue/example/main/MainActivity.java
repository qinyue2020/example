package com.qinyue.example.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.qinyue.example.BR;
import com.qinyue.example.R;
import com.qinyue.example.base.MyBaseActivity;
import com.qinyue.example.databinding.ActivityMainBinding;
import com.qinyue.example.main.adapter.MainPageAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;

/**
 * 主页
 */
public class MainActivity extends MyBaseActivity<ActivityMainBinding, MainViewModel> implements ViewPager.OnPageChangeListener {
    @Titles
    private static final String[] mTitles = {"页面一", "网页", "页面三", "我的"};

    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.nav_01_pre, R.mipmap.nav_01_pre, R.mipmap.nav_01_pre, R.mipmap.nav_01_pre};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.nav_01_nor, R.mipmap.nav_01_nor, R.mipmap.nav_01_nor, R.mipmap.nav_01_nor};

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public String initTitleText() {
        return "我的自定义";
    }

    @Override
    protected void onClickTitleLeft() {
        super.onClickTitleLeft();
        ToastUtils.showShort("hahahah");
        setTitleIconLeftVisible(false);
    }

    @Override
    public void initData() {
        super.initData();
        getTitleBar().addAction(new TitleBar.ImageAction(R.drawable.ic_white_add) {
            @Override
            public void performAction(View view) {
                ToastUtils.showShort("右边加按钮");
            }
        });
        //页面可以滑动
        binding.tabbar.setGradientEnable(true);
        binding.tabbar.setPageAnimateEnable(true);
        binding.tabbar.setTabTypeFace(XUI.getDefaultTypeface());
        binding.viewPager.setOffscreenPageLimit(mTitles.length);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager(), this, mTitles));
        binding.tabbar.setContainer(binding.viewPager);

        if (binding.tabbar.getMiddleView() != null) {
            binding.tabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onClick(View v) {
                    //请求打开相机权限
                    RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                    rxPermissions.request(Manifest.permission.CAMERA)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (aBoolean) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                                        startActivityForResult(intent, 12);
                                        ToastUtils.showShort("权限已经打开，直接跳入相机");
                                    } else {
                                        ToastUtils.showShort("权限被拒绝");
                                    }
                                }
                            });
//                    ToastUtils.showShort("测试获取权限");
                }
            });
        }

        binding.tabbar.showBadge(2, "", true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int index) {
        if (index == 2) {
            binding.tabbar.hideBadge(2);
        }
        setTitleText(mTitles[index]);
        ToastUtils.showShort("点击了" + mTitles[index]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
