package com.xht97.whulibraryseat.model.impl;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.IUserInfoModel;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.bean.User;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

import java.util.List;


public class UserInfoModelImpl implements IUserInfoModel {

    private HttpUtil httpUtil = HttpUtil.getInstance();

    @Override
    public void getUserInfo(final BasePresenter.BaseRequestCallback<User> callback) {

        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.USER_INFO, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                User user = JsonUtil.getUserInfo(data);
                if (user != null) {
                    callback.onSuccess(user);
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
    public void getReserve(final BasePresenter.BaseRequestCallback<List<Reserve>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.USER_RESERVE, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {

                httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.USER_RESERVE, new HttpUtil.HttpCallBack() {
                    @Override
                    public void onSuccess(String data) {
                        List<Reserve> list = JsonUtil.getReserveList(data);
                        if (list != null) {
                            callback.onSuccess(list);
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
        });
    }

    @Override
    public void getReserveHistory(final BasePresenter.BaseRequestCallback<List<ReserveHistory>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.USER_RESERVE_HISTORY + "50", new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<ReserveHistory> list = JsonUtil.getReserveHistoryList(data);
                if (list != null) {
                    callback.onSuccess(list);
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
    public void getViolation(BasePresenter.BaseRequestCallback callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.USER_VIOLATION_HISTORY, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                // TODO
            }
        });
    }
}
