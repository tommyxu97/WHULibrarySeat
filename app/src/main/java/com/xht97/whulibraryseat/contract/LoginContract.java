package com.xht97.whulibraryseat.contract;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;

public interface LoginContract {

    interface ILoginView extends BaseView {

        public String getUserId();

        public String getPassword();

    }

    abstract class AbstractLoginPresenter extends BasePresenter {

        public abstract void login();

    }

}
