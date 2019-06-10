package com.xht97.whulibraryseat.presenter;

import com.xht97.whulibraryseat.contract.MainContract;
import com.xht97.whulibraryseat.model.impl.LoginModelImpl;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.NetworkUtil;

public class MainPresenter extends MainContract.AbstractMainPresenter {

    @Override
    public void updateToken() {

        if (!NetworkUtil.isNetworkConnected(getView())) {
            getView().showMessage("主人，我貌似访问不了互联网哦");
            getView().showEmptyView();
            return;
        }

        LoginModelImpl model = new LoginModelImpl();
        getView().showLoading();
        model.login(AppDataUtil.getMainId(), AppDataUtil.getMainPassword(), new BaseRequestCallback<String>() {
            @Override
            public void onSuccess(String data) {
                super.onSuccess(data);
                getView().hideLoading();
                getView().showMessage(data);

                // 更新token后才能对三个界面进行初始化
                getView().initFragment();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                if (message.equals("timeout")) {
                    message = "图书馆选座服务器的电波无法到达，可能服务器酱需要休息一下，请稍后再来哦";
                }
                getView().showMessage(message);
                getView().hideLoading();
                getView().showEmptyView();
            }
        });

    }
}
