package com.xht97.whulibraryseat.model;

import android.content.Context;

import com.xht97.whulibraryseat.base.BasePresenter;

public interface ILoginModel {

    void login(String userId, String password, BasePresenter.BaseRequestCallback<String> callback);

    void logout(BasePresenter.BaseRequestCallback<String> callback);

    boolean savePassword(String studentId, String password);

    String getPassword(String studentId);

    boolean deletePassword(String studentId);

}
