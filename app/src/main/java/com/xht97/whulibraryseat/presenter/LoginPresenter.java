package com.xht97.whulibraryseat.presenter;

import com.xht97.whulibraryseat.contract.LoginContract;
import com.xht97.whulibraryseat.model.impl.LoginModelImpl;

public class LoginPresenter extends LoginContract.AbstractLoginPresenter {

    private LoginModelImpl model = new LoginModelImpl();

    @Override
    public void login(String studentId, String password) {

        getView().showLoading();

        model.login(studentId, password, new BaseRequestCallback<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                getView().hideLoading();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showError();
            }
        });
    }

    @Override
    public void savePassword(String studentId, String password) {

        boolean flag = model.savePassword(studentId, password);
        if (!flag) {
            getView().showError();
        }

    }

}
