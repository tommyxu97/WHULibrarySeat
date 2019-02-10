package com.xht97.whulibraryseat.presenter;

import android.content.Intent;

import com.xht97.whulibraryseat.app.StaticVar;
import com.xht97.whulibraryseat.contract.LoginContract;
import com.xht97.whulibraryseat.model.impl.LoginModelImpl;
import com.xht97.whulibraryseat.ui.activity.LoginActivity;
import com.xht97.whulibraryseat.util.AppDataUtil;

public class LoginPresenter extends LoginContract.AbstractLoginPresenter {

    private LoginModelImpl model = new LoginModelImpl();

    @Override
    public void login(final String studentId, final String password) {

        if (studentId.length() != 13) {
            getView().showErrorFocus(LoginActivity.STUDENTID_INPUT, "学号格式不正确");
            return;
        }
        if (password.length() == 0) {
            getView().showErrorFocus(LoginActivity.PASSWORD_INPUT, "此项为必填项");
            return;
        }

        setAutoLogin(true);
        // 检验完学号与密码的格式正确后开始网络请求进行登录
        getView().showLoading();
        model.login(studentId, password, new BaseRequestCallback<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                getView().hideLoading();
                getView().showMessage(data);

                // 登录成功后如果用户开启了自动登录功能后，将账号密码保存在本地以供下一次自动登录
                boolean flag = model.savePassword(studentId, password);
                if (!flag) {
                    getView().showMessage("保存用户名与密码到本地失败");
                }

                Intent intent = new Intent();
                intent.putExtra(StaticVar.LOGIN_SUCCESS, true);
                getView().setResult(android.app.Activity.RESULT_OK, intent);
                getView().finish();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().hideLoading();
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void setAutoLogin(boolean flag) {
        model.setAutoLogin(flag);
    }

    @Override
    public boolean getAutoLogin() {
        return model.getAutoLogin();
    }

    public void fillPassword() {
        if (AppDataUtil.isPasswordExists()) {
            getView().getStudentId().setText(AppDataUtil.getMainId());
            getView().getPasswordView().setText(AppDataUtil.getMainPassword());
        }
    }
}
