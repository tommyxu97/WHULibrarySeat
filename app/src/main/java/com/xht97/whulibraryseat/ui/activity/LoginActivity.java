package com.xht97.whulibraryseat.ui.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseActivity;
import com.xht97.whulibraryseat.base.BasePresenter;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}