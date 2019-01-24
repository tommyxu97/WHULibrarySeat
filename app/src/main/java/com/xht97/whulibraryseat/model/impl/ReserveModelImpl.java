package com.xht97.whulibraryseat.model.impl;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.IReserveModel;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

import java.util.List;

public class ReserveModelImpl implements IReserveModel {

    private HttpUtil httpUtil = HttpUtil.getInstance();

    @Override
    public void getAvailableDate(final BasePresenter.BaseRequestCallback<List<String>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.BUILDING_INFO, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List dates = JsonUtil.getDateList(data);
                if (dates != null) {
                    callback.onSuccess(dates);
                } else {
                    callback.onError(JsonUtil.getErrorMessage(data));
                }
            }

            @Override
            public void onError(String msg) {
                super.onError(msg);
                callback.onError(msg);
            }
        });
    }

    @Override
    public void getBuildingStatus(int buildingId, BasePresenter.BaseRequestCallback<List<Room>> callback) {

    }

    @Override
    public void getRoomStatus(int roomId, BasePresenter.BaseRequestCallback<List<Seat>> callback) {

    }

    @Override
    public void getSeatStartTime(int seatId, BasePresenter.BaseRequestCallback<List<SeatTime>> callback) {

    }

    @Override
    public void getSeatEndTime(int seatId, String starTime, BasePresenter.BaseRequestCallback<List<SeatTime>> callback) {

    }

    @Override
    public void reserveSeat(int seatId, String date, String startTime, String endTime, BasePresenter.BaseRequestCallback<Reserve> callback) {

    }

    @Override
    public void cancelSeat(int bookId, BasePresenter.BaseRequestCallback<String> callback) {

    }

    @Override
    public void selectSeatByTime(int buildingId, int roomId, String date, String startTime, String endTime, BasePresenter.BaseRequestCallback<List<Seat>> callback) {

    }

}
