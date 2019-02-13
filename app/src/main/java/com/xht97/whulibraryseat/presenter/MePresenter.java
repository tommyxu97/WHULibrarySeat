package com.xht97.whulibraryseat.presenter;

import com.xht97.whulibraryseat.contract.MeContract;
import com.xht97.whulibraryseat.model.bean.User;
import com.xht97.whulibraryseat.model.impl.UserInfoModelImpl;

public class MePresenter extends MeContract.AbstractMePresenter {

    private UserInfoModelImpl userInfoModel = new UserInfoModelImpl();

    @Override
    public void updateUserData() {
        userInfoModel.getUserInfo(new BaseRequestCallback<User>() {
            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);

                getView().getUserNameView().setText(data.getName());
                getView().getUserIdView().setText(data.getStudentId());
                getView().getUserStatusView().setText(data.getStatus());
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }
}
