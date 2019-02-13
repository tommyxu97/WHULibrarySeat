package com.xht97.whulibraryseat.contract;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;
import com.xht97.whulibraryseat.ui.fragment.MeFragment;

public interface MeContract {

    interface IMeView extends BaseView {

    }

    abstract class AbstractMePresenter extends BasePresenter<MeFragment> {

        public abstract void updateUserData();
    }

}
