package com.xht97.whulibraryseat.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseFragment;
import com.xht97.whulibraryseat.presenter.FunctionPresenter;

public class FunctionFragment extends BaseFragment<FunctionFragment, FunctionPresenter> {


    public FunctionFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_function, container, false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected FunctionPresenter createPresenter() {
        return new FunctionPresenter();
    }
}
