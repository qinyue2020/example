package com.qinyue.example.main.fragment.first;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qinyue.example.BR;
import com.qinyue.example.R;
import com.qinyue.example.databinding.FragmentFirstBinding;

import androidx.annotation.Nullable;
import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * 创建人:qinyue
 * 创建日期:2020/2/27
 * 描述:
 **/
public class FirstFragment extends BaseFragment<FragmentFirstBinding,FirstViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_first;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
