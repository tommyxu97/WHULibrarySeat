package com.xht97.whulibraryseat.contract;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;
import com.xht97.whulibraryseat.ui.activity.LoginActivity;

public interface LoginContract {

    interface ILoginView extends BaseView {

        String getUserId();

        String getPassword();

    }

    abstract class AbstractLoginPresenter extends BasePresenter<LoginActivity> {

        public abstract void login(String studentId, String password);

        public abstract void savePassword(String studentId, String password);
    }

}
