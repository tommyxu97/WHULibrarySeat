package com.xht97.whulibraryseat.model;

import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;

import java.util.List;

public interface IReserveModel {

    void getAvailableDate(BasePresenter.BaseRequestCallback<List<String>> callback);

    void getBuildingStatus(int buildingId, BasePresenter.BaseRequestCallback<List<Room>> callback);

    void getRoomStatus(int roomId, String date, BasePresenter.BaseRequestCallback<List<Seat>> callback);

    void getSeatStartTime(int seatId, String date, BasePresenter.BaseRequestCallback<List<SeatTime>> callback);

    void getSeatEndTime(int seatId, String date, String startTime, BasePresenter.BaseRequestCallback<List<SeatTime>> callback);

    void reserveSeat(int seatId, String date, String startTime, String endTime, BasePresenter.BaseRequestCallback<InstantReserve> callback);

    void cancelSeat(int bookId, BasePresenter.BaseRequestCallback<String> callback);

    void selectSeatByTime(int buildingId, int roomId, String date, String startTime, String endTime, BasePresenter.BaseRequestCallback<List<Seat>> callback);

}
