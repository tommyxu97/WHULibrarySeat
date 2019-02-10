package com.xht97.whulibraryseat.presenter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.xht97.whulibraryseat.contract.ReserveContract;
import com.xht97.whulibraryseat.model.bean.Building;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.model.impl.ReserveModelImpl;
import com.xht97.whulibraryseat.model.impl.UserInfoModelImpl;
import com.xht97.whulibraryseat.ui.adapter.BuildingAdapter;
import com.xht97.whulibraryseat.ui.adapter.DateAdapter;
import com.xht97.whulibraryseat.ui.adapter.RoomAdapter;
import com.xht97.whulibraryseat.ui.adapter.SeatAdapter;
import com.xht97.whulibraryseat.ui.listener.ClickListener;
import com.xht97.whulibraryseat.util.AppDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ReservePresenter extends ReserveContract.AbstractReservePresenter {

    private final String TAG = "ReserveFragment";

    private ReserveModelImpl reserveModel = new ReserveModelImpl();
    private UserInfoModelImpl userModel = new UserInfoModelImpl();

    private RoomAdapter roomAdapter;
    private SeatAdapter seatAdapter;

    private int[] roomLayout;

    // 记录用户所选的选项
    private String date;
    private int building = AppDataUtil.getLastSelectedBuilding();
    private int roomId;
    private int seatId;
    private String startTime;
    private String endTime;

    @Override
    public void setCurrentReserve() {
        userModel.getReserve(new BaseRequestCallback<List<Reserve>>() {
            @Override
            public void onSuccess(List<Reserve> data) {
                super.onSuccess(data);
                if (!data.isEmpty()) {
                    // 取出当前用户正在执行的预约
                    Reserve reserve = data.get(0);
                    String location = reserve.getLocation();
                    String startTime = reserve.getBegin().replace(" ", "");
                    String endTime = reserve.getEnd().replace(" ", "");
                    String status = reserve.getStatus();

                    String title = "您正在使用的座位为：" + "(" + status + ")";
                    String detail = location + "，" + startTime + "-" + endTime;

                    getView().getStatusTitle().setText(title);
                    getView().getStatusDetail().setText(detail);
                } else {
                    getView().getStatusTitle().setText("您目前没有正在使用的预约");
                    getView().getStatusDetail().setText("小提示：正在使用的座位会在这里显示哦");
                }
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void setAvailableTime() {
        reserveModel.getAvailableDate(new BaseRequestCallback<List<List>>() {
            @Override
            public void onSuccess(final List<List> lists) {
                super.onSuccess(lists);
                if (!lists.isEmpty()) {
                    final List<String> dates = lists.get(0);
                    final List<Building> buildings = lists.get(1);

                    // 设置可选的日期
                    // 默认为今天
                    date = dates.get(0);
                    DateAdapter dateAdapter = new DateAdapter(getView().getActivity(), dates);
                    getView().getDateSpinner().setAdapter(dateAdapter);
                    getView().getDateSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            date = dates.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    // 设置可选的场馆
                    // 默认为上次选取的场馆
                    int buildingIndex = building - 1;
                    BuildingAdapter buildingAdapter = new BuildingAdapter(getView().getActivity(), buildings);
                    getView().getBuildingSpinner().setAdapter(buildingAdapter);
                    getView().getBuildingSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Building buildingObject = buildings.get(position);
                            building = buildingObject.getId();
                            AppDataUtil.putLastSelectedBuilding(building);
                            // 更新场馆时刷新页面
                            getRooms();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    getView().getBuildingSpinner().setSelection(buildingIndex);
                }
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void getRooms() {
        reserveModel.getBuildingStatus(building, new BaseRequestCallback<List<Room>>() {
            @Override
            public void onSuccess(List<Room> data) {
                super.onSuccess(data);
                roomAdapter.updateData(data);
                getView().hideLoading();
                getView().getRoomView().setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });

    }

    @Override
    public void getSeats() {
        reserveModel.getRoomStatus(roomId, date, new BaseRequestCallback<List<Seat>>() {
            @Override
            public void onSuccess(List<Seat> data, int[] layout) {
                super.onSuccess(data);
                roomLayout = layout;
                seatAdapter.updateData(data);
                getView().hideLoading();
                getView().getSeatView().setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void getSeatStartTime() {
        reserveModel.getSeatStartTime(seatId, date, new BaseRequestCallback<List<SeatTime>>() {
            @Override
            public void onSuccess(List<SeatTime> data) {
                super.onSuccess(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void getSeatEndTime() {
        reserveModel.getSeatEndTime(seatId, startTime, date, new BaseRequestCallback<List<SeatTime>>() {
            @Override
            public void onSuccess(List<SeatTime> data) {
                super.onSuccess(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void reserve() {
        reserveModel.reserveSeat(seatId, date, startTime, endTime, new BaseRequestCallback<InstantReserve>() {
            @Override
            public void onSuccess(InstantReserve data) {
                super.onSuccess(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });
    }

    @Override
    public void setAdapter() {
        // 这些操作均在Fragment初始化时完成
        final RecyclerView roomView = getView().getRoomView();
        RecyclerView seatView = getView().getSeatView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getActivity());
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        roomAdapter = new RoomAdapter(getView().getActivity(), new ArrayList<Room>());
        roomAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Room room = roomAdapter.getRoomByPosition(position);
                roomId = room.getRoomId();

                Log.d(TAG, "选中" + room.getRoom());
                // 当某个room被选中时，记录下roomId并且进入加载状态，请求座位信息，并在请求完成seats后hideLoading
                getView().showLoading();
                getSeats();
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        roomView.setLayoutManager(layoutManager);
        roomView.setAdapter(roomAdapter);
        roomView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getView().getActivity(), 5);
        seatAdapter = new SeatAdapter(getView().getActivity(), new ArrayList<Seat>());
        seatAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Seat seat = seatAdapter.getSeatByPosition(position);
                seatId = seat.getId();

                Log.d(TAG, "用户选中了座位：" + seat.getId() + "，座位号为：" + seat.getName());
                // todo 点击完成后并弹出对话框让用户选择时间
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        seatView.setLayoutManager(layoutManager2);
        seatView.setAdapter(seatAdapter);
        seatView.setItemAnimator(new DefaultItemAnimator());
    }
}
