package com.qinyue.example.main.adapter;

import android.content.Context;
import android.view.View;

import com.qinyue.example.main.fragment.first.FirstFragment;
import com.qinyue.example.main.fragment.my.MyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/27
 * 描述:主页的适配器
 **/
public class MainPageAdapter extends FragmentPagerAdapter {
    private Context context;
    String[] mTitles;
    List<Fragment> fragments = new ArrayList<>();
    public MainPageAdapter(FragmentManager fm, Context context, String[] mTitles){
        super(fm);
        this.context =context;
        this.mTitles =mTitles;
        fragments.add(new FirstFragment());
        fragments.add(new FirstFragment());
        fragments.add(new FirstFragment());
        fragments.add(new MyFragment());
    }
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

//    @Override
//    public Object instantiateItem(final ViewGroup container, int position) {
//        View view = getPageView(mTitles[position]);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        container.addView(view, params);
//        return view;
//    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//    private View getPageView(String pageName) {
//        View view = mPageMap.get(pageName);
//        if (view == null) {
//            TextView textView = new TextView(context);
//            textView.setTextAppearance(context, R.style.TextAppearance_AppCompat_Display1);
//            textView.setGravity(Gravity.CENTER);
//            textView.setText(String.format("这个是%s页面的内容", pageName));
//            view = textView;
//            mPageMap.put(pageName, view);
//        }
//        return view;
//    }
}
