package com.xht97.whulibraryseat.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.app.MyApplication;
import com.xht97.whulibraryseat.model.ILoginModel;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

public class LoginModelImpl implements ILoginModel {

    private SharedPreferences data = MyApplication.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
    private SharedPreferences account = MyApplication.getContext().getSharedPreferences("Account", Context.MODE_PRIVATE);

    /**
     * 根据用户的用户名和密码进行登录操作
     * 登录成功后，会在回调中返回登陆成功，并且将token存储到sharedpref中
     */
    @Override
    public void login(String userId, String password, final BasePresenter.BaseRequestCallback<String> callback) {

        HttpUtil httpUtil = HttpUtil.getInstance();
        String url = WHUSeatApi.USER_LOGIN + "username=" + userId + "&password=" + password;
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), url, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                String token = JsonUtil.getToken(data);
                if(!token.equals("ERROR")) {
                    if (LoginModelImpl.this.data.edit().putString("token", token).commit()) {
                        callback.onSuccess("登录成功");
                    }
                } else {
                    callback.onError(JsonUtil.getErrorMessage(data));
                }
            }

            @Override
            public void onError(String msg) {
                super.onError(msg);
                callback.onError(msg);
            }
        });

    }

    /**
     * 用户登出
     * 暂时没有实现
     */
    @Override
    public void logout(BasePresenter.BaseRequestCallback<String> callback) {

    }

    /**
     * 在本地sharedpref中存储主账号和密码
     * 结果返回boolean变量
     */
    @Override
    public boolean savePassword(String studentId, String password) {

        boolean flag1 = account.edit().putString("id1", studentId).commit();
        boolean flag2 = account.edit().putString("pwd1", password).commit();

        return flag1 && flag2;
    }

    /**
     * 获取本地存储的主账号
     */
    @Override
    public String getStudentId() {
        return account.getString("id1", "ERROR");
    }

    /**
     * 获取本地存储的主密码
     */
    @Override
    public String getPassword() {

        return account.getString("pwd1", "ERROR");

    }

    /**
     * 删除主密码
     * 返回boolean变量
     */
    @Override
    public boolean deletePassword() {

        return account.edit().remove("pwd1").commit();

    }

    /**
     * 设置是否开启自动登录
     * 返回boolean变量
     */
    @Override
    public boolean setAutoLogin(boolean flag) {
        return data.edit().putBoolean("autoLogin", flag).commit();
    }

    /**
     * 获取自动登录的本地配置
     * 返回boolean变量
     */
    @Override
    public boolean getAutoLogin() {
        return data.getBoolean("autoLogin", true);
    }
}
