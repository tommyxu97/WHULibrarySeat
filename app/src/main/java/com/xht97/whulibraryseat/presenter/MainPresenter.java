package com.xht97.whulibraryseat.presenter;

import com.xht97.whulibraryseat.contract.MainContract;
import com.xht97.whulibraryseat.model.impl.LoginModelImpl;
import com.xht97.whulibraryseat.ui.fragment.FunctionFragment;
import com.xht97.whulibraryseat.ui.fragment.MeFragment;
import com.xht97.whulibraryseat.ui.fragment.ReserveFragment;
import com.xht97.whulibraryseat.util.AppDataUtil;

public class MainPresenter extends MainContract.AbstractMainPresenter {

    @Override
    public void updateToken() {

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
                getView().showMessage(message);
                getView().hideLoading();
            }
        });

    }
}
