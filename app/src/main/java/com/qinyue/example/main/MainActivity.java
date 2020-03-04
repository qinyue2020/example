package com.qinyue.example.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
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
            @SuppressLint("CheckResult")
            @Override
            public void performAction(View view) {
                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    new ShareAction(MainActivity.this).withText("分享的链接").withMedia(new UMWeb("www.baidu.com")).withMedia(new UMImage(MainActivity.this, R.mipmap.ic_launcher_round)).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                                            .setCallback(new UMShareListener() {
                                                @Override
                                                public void onStart(SHARE_MEDIA share_media) {

                                                }

                                                @Override
                                                public void onResult(SHARE_MEDIA share_media) {
                                                    ToastUtils.showShort("分享成功");
                                                }

                                                @Override
                                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                    ToastUtils.showShort("分享失败"+throwable.getMessage());
                                                }

                                                @Override
                                                public void onCancel(SHARE_MEDIA share_media) {
                                                    ToastUtils.showShort("取消分享");
                                                }
                                            }).open();
                                } else {
                                    ToastUtils.showShort("权限被拒绝");
                                }
                            }
                        });

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
