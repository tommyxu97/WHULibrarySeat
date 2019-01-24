package com.xht97.whulibraryseat.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BasePresenter<T> {

    private Reference<T> mViewRef;

    public void attachView(T mView) {
        mViewRef = new WeakReference<>(mView);
    }

    protected T getView() {
        if(isViewAttached()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public void detachView() {
        if(mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    private boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public interface BaseRequestCallback<T> {

        /**
         * Description: 请求开始回调
         * CreateTime: 2019/1/20
         */
        void onStart();

        /**
         * Description: 请求成功时回调
         * CreateTime: 2019/1/20
         * @param data 泛型T类型的数据
         */
        void onSuccess(T data);

        /**
         * Description: 请求失败时调用
         * CreateTime: 2019/1/20
         * @param message String类型的错误信息
         */
        void onError(String message);

    }

}
