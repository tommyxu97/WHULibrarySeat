package com.xht97.whulibraryseat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if(mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        initView();

        // 当正常启动activity时初始化数据
        if (savedInstanceState == null) {
            initData();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPresenter != null) {
            mPresenter.detachView();
        }

    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract T createPresenter();

}
