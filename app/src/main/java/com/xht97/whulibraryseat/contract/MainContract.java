package com.xht97.whulibraryseat.contract;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;
import com.xht97.whulibraryseat.ui.activity.MainActivity;

public interface MainContract {

    interface IMainView extends BaseView {

        void setFabFunction(int type);

        void setUiMode(int mode);

        int getUiMode();
    }

    abstract class AbstractMainPresenter extends BasePresenter<MainActivity> {

        public abstract void updateToken();

    }

}
