package com.xht97.whulibraryseat.model.impl;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.ISeatActionModel;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

public class SeatActionModelImpl implements ISeatActionModel {

    private HttpUtil httpUtil = HttpUtil.getInstance();

    @Override
    public void checkIn(final BasePresenter.BaseRequestCallback<String> callback) {

        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.CHECKIN, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (JsonUtil.isRequestSuccessful(data)) {
                    callback.onSuccess("操作成功");
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
    public void leave(final BasePresenter.BaseRequestCallback<String> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.LEAVE, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (JsonUtil.isRequestSuccessful(data)) {
                    callback.onSuccess("操作成功");
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
    public void stop(final BasePresenter.BaseRequestCallback<String> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.STOP, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (JsonUtil.isRequestSuccessful(data)) {
                    callback.onSuccess("操作成功");
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
}
