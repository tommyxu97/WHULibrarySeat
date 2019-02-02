package com.xht97.whulibraryseat.model.impl;

import com.xht97.whulibraryseat.api.WHUSeatApi;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.IReserveModel;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.util.AppDataUtil;
import com.xht97.whulibraryseat.util.HttpUtil;
import com.xht97.whulibraryseat.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReserveModelImpl implements IReserveModel {

    private HttpUtil httpUtil = HttpUtil.getInstance();

    /**
     * 获取目前可选座位的日期
     * 返回为String[]
     */
    @Override
    public void getAvailableDate(final BasePresenter.BaseRequestCallback<List<String>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.BUILDING_INFO, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<String> dates = JsonUtil.getDateList(data);
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

    /**
     * 获取每个场馆的整体信息
     * 可以返回List<Room>
     */
    @Override
    public void getBuildingStatus(int buildingId, final BasePresenter.BaseRequestCallback<List<Room>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.BUILDING_STATUS + buildingId, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<Room> rooms = JsonUtil.getRoomList(data);
                if (rooms != null) {
                    callback.onSuccess(rooms);
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

    /**
     * 根据用户选择的场馆，查询目前的座位情况
     * 返回值为当前该场馆所有的位置信息，以及当前楼层的布局信息
     */
    @Override
    public void getRoomStatus(int roomId, String date, final BasePresenter.BaseRequestCallback<List<Seat>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.ROOM_STATUS + roomId + "/" + date, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<Seat> seats = JsonUtil.getSeats(data);
                int[] layout = JsonUtil.getLayoutParameter(data);
                if (seats != null && layout != null) {
                    callback.onSuccess(seats, layout);
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

    /**
     * 根据用户选择的座位，查询可开始的时间
     * 返回开始时间
     */
    @Override
    public void getSeatStartTime(int seatId, String date, final BasePresenter.BaseRequestCallback<List<SeatTime>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.SEAT_START_TIME + seatId + "/" + date, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<SeatTime> seatTimes = JsonUtil.getSeatStartTime(data);
                if (seatTimes != null) {
                    callback.onSuccess(seatTimes);
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

    /**
     * 根据用户选择的位置与开始时间，查询座位可用的结束时间
     * 返回结束时间
     */
    @Override
    public void getSeatEndTime(int seatId, String date, String startTime, final BasePresenter.BaseRequestCallback<List<SeatTime>> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.SEAT_END_TIME + seatId + "/" + date + "/" + startTime, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<SeatTime> seatTimes = JsonUtil.getSeatEndTime(data);
                if (seatTimes != null) {
                    callback.onSuccess(seatTimes);
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
    public void reserveSeat(int seatId, String date, String startTime, String endTime, final BasePresenter.BaseRequestCallback<InstantReserve> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("t", "1");
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("seat", String.valueOf(seatId));
        map.put("date", date);
        map.put("t2", "2");

        httpUtil.postWithToken(WHUSeatApi.RESERVE, AppDataUtil.getTokenFromLocal(), map, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                InstantReserve reserve = JsonUtil.getInstantReserve(data);
                if (reserve != null) {
                    callback.onSuccess(reserve);
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
    public void cancelSeat(int bookId, final BasePresenter.BaseRequestCallback<String> callback) {
        httpUtil.getWithToken(AppDataUtil.getTokenFromLocal(), WHUSeatApi.CANCEL + bookId, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                if (JsonUtil.isRequestSuccessful(data)) {
                    callback.onSuccess("取消预约成功");
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
    public void selectSeatByTime(int buildingId, int roomId, String date, String startTime, String endTime, final BasePresenter.BaseRequestCallback<List<Seat>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("t", "1");
        map.put("roomId", String.valueOf(roomId));
        map.put("buildingId", String.valueOf(buildingId));
        map.put("batch", "9999");
        map.put("page", "1");
        map.put("t2", "2");

        httpUtil.postWithToken(WHUSeatApi.SELECT_SEAT, AppDataUtil.getTokenFromLocal(), map, new HttpUtil.HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                List<Seat> seats = JsonUtil.getSeatsByTime(data);
                if (seats != null) {
                    callback.onSuccess(seats);
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

}
