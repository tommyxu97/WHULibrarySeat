package com.xht97.whulibraryseat.contract;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;
import com.xht97.whulibraryseat.ui.fragment.ReserveFragment;

public interface ReserveContract {

    interface IReserveView extends BaseView {



    }

    abstract class AbstractReservePresenter extends BasePresenter<ReserveFragment> {

        public abstract void setCurrentReserve();

        public abstract void setAvailableTime();

        public abstract void getRooms();

        public abstract void getSeats();

        public abstract void getSeatStartTime();

        public abstract void getSeatEndTime();

        public abstract void reserve();

        public abstract void setAdapter();

    }

}
