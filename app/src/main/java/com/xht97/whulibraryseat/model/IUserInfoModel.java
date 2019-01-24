package com.xht97.whulibraryseat.model;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.bean.User;

import java.util.List;

public interface IUserInfoModel {

    void getUserInfo(BasePresenter.BaseRequestCallback<User> callback);

    void getReserve(BasePresenter.BaseRequestCallback<List<Reserve>> callback);

    void getReserveHistory(BasePresenter.BaseRequestCallback<List<ReserveHistory>> callback);

    void getViolation(BasePresenter.BaseRequestCallback callback);

}
