package com.xht97.whulibraryseat.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.MyApplication;
import com.xht97.whulibraryseat.model.ILoginModel;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

public class LoginModelImpl implements ILoginModel {

    private SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);

    @Override
    public void login(String userId, String password, final BasePresenter.BaseRequestCallback<String> callback) {

        HttpUtil httpUtil = HttpUtil.getInstance();
        String url = WHUSeatApi.USER_LOGIN + "username=" + userId + "&password=" + password;
        httpUtil.getWithToken("NEYIJFUPX101195712", url, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                String token = JsonUtil.getToken(data);
                if(!token.equals("ERROR")) {
                    if (preferences.edit().putString("token", token).commit()) {
                        callback.onSuccess("登陆成功");
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

    @Override
    public void logout(BasePresenter.BaseRequestCallback<String> callback) {

    }

    @Override
    public boolean savePassword(String studentId, String password) {

        return preferences.edit().putString(studentId, password).commit();

    }

    @Override
    public String getPassword(String studentId) {

        return preferences.getString(studentId, "ERROR");

    }

    @Override
    public boolean deletePassword(String studentId) {

        return preferences.edit().remove(studentId).commit();

    }
}
