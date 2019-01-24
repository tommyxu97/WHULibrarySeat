package com.xht97.whulibraryseat.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    protected View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();

        if(mPresenter != null) {
            mPresenter.attachView((V) this);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initView();

        initData();

        return mRootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(mPresenter != null) {
            mPresenter.detachView();
        }

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract T createPresenter();

}
