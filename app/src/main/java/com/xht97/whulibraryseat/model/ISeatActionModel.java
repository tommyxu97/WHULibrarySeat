package com.xht97.whulibraryseat.model;

import com.xht97.whulibraryseat.base.BasePresenter;

public interface ISeatActionModel {

    void checkIn(BasePresenter.BaseRequestCallback<String> callback);

    void leave(BasePresenter.BaseRequestCallback<String> callback);

    void stop(BasePresenter.BaseRequestCallback<String> callback);

}
