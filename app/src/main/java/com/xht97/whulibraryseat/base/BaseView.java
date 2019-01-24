package com.xht97.whulibraryseat.base;

public interface BaseView<T> {

    void setPresenter(T presenter);

    void showLoading();

    void hideLoading();

    void showError();

    void showEmptyView();
}
