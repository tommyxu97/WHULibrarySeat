package com.xht97.whulibraryseat.ui.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseActivity;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.contract.LoginContract;
import com.xht97.whulibraryseat.presenter.LoginPresenter;


public class LoginActivity extends BaseActivity implements LoginContract.ILoginView{

    @Override
    protected void initView() {
        // 初始化所有界面上的控件
        setContentView(R.layout.activity_login);


    }

    @Override
    protected void initData() {
        // 初始化数据
    }

    @Override
    protected BasePresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}