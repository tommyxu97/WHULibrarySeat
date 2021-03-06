package com.xht97.whulibraryseat.presenter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;
import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.contract.ReserveContract;
import com.xht97.whulibraryseat.model.bean.Building;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.model.bean.StaredSeat;
import com.xht97.whulibraryseat.model.impl.ReserveModelImpl;
import com.xht97.whulibraryseat.model.impl.SeatActionModelImpl;
import com.xht97.whulibraryseat.model.impl.UserInfoModelImpl;
import com.xht97.whulibraryseat.ui.activity.MainActivity;
import com.xht97.whulibraryseat.ui.adapter.BuildingAdapter;
import com.xht97.whulibraryseat.ui.adapter.DateAdapter;
import com.xht97.whulibraryseat.ui.adapter.RoomAdapter;
import com.xht97.whulibraryseat.ui.adapter.SeatAdapter;
import com.xht97.whulibraryseat.ui.adapter.SeatTimeAdapter;
import com.xht97.whulibraryseat.ui.listener.ClickListener;
import com.xht97.whulibraryseat.ui.weight.SeatChooserDialog;
import com.xht97.whulibraryseat.ui.weight.SeatLayoutView;
import com.xht97.whulibraryseat.ui.weight.TimeChooserDialog;
import com.xht97.whulibraryseat.util.AppDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ReservePresenter extends ReserveContract.AbstractReservePresenter {

    private final String TAG = "ReserveFragment";
    private MainActivity activity;

    private ReserveModelImpl reserveModel = new ReserveModelImpl();
    private UserInfoModelImpl userModel = new UserInfoModelImpl();
    private SeatActionModelImpl seatActionModel = new SeatActionModelImpl();

    private RoomAdapter roomAdapter;
    private SeatAdapter seatAdapter;

    private int currentReserveId;
    private List<Seat> seatList = new ArrayList<>();
    private int[] roomLayout = new int[] {0, 0};

    // 记录用户所选的选项
    private String date;
    private int buildingId = AppDataUtil.getLastSelectedBuilding();
    private int roomId;
    private int seatId;
    private String startTime;
    private String endTime;

    // 收藏座位时需要使用楼栋名字，房间名字
    private String buildingName = AppDataUtil.getLastSelectedBuildingName();
    private String roomName;

    /**
     * ReservePresenter
     */
    public ReservePresenter() {
    }

    /**
     * 在用户界面上显示目前的预约状态
     */
    @Override
    public void setCurrentReserve() {
        userModel.getReserve(new BaseRequestCallback<List<Reserve>>() {
            @Override
            public void onSuccess(List<Reserve> data) {
                super.onSuccess(data);
                if (!data.isEmpty()) {
                    // 取出当前用户正在执行的预约
                    Reserve reserve = data.get(0);

                    currentReserveId = reserve.getId();
                    String location = reserve.getLocation();
                    String startTime = reserve.getBegin().replace(" ", "");
                    String endTime = reserve.getEnd().replace(" ", "");
                    String status = reserve.getStatus();

                    String title = "您正在使用的座位：" + "(状态为" + status + ")";
                    String detail = location + "，" + startTime + "-" + endTime;

                    getView().getStatusTitle().setText(title);
                    getView().getStatusDetail().setText(detail);

                    // 将主页上的FAB改为停止使用座位的点击模式
                    if (reserve.getStatus().equals("CHECK_IN") || reserve.getStatus().equals("AWAY")) {
                        setFabStopUsing();
                    } else {
                        // FIX_BUG：当程序从后台恢复运行时，用户的预约已经完成，但是fab仍然显示为停止按钮
                        setFabLogin();
                    }
                } else {
                    if (getView() != null) {
                        getView().getStatusTitle().setText("您目前没有正在使用的预约");
                        getView().getStatusDetail().setText("小提示：正在使用的座位会在这里显示哦");
                    }
                }
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().hideLoading();
                getView().showMessage(message);
            }
        });
    }

    /**
     * 初始化可以用户可以预约座位的日期与场馆
     */
    @Override
    public void setAvailableTime() {
        activity = (MainActivity) getView().getActivity();

        reserveModel.getAvailableDate(new BaseRequestCallback<List<List>>() {
            @Override
            public void onSuccess(final List<List> lists) {
                super.onSuccess(lists);
                if (!lists.isEmpty()) {
                    final List<String> dates = lists.get(0);
                    final List<Building> buildings = lists.get(1);

                    // 设置可选的日期
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
                    // 默认为今天，但是可选明天时设置为第二天
                    if (dates.size() == 1) {
                        date = dates.get(0);
                    } else if (dates.size() == 2){
                        date = dates.get(1);
                        getView().getDateSpinner().setSelection(1);
                    } else {
                        date = dates.get(0);
                    }

                    // 设置可选的场馆
                    // 默认为上次选取的场馆
                    int buildingIndex = buildingId - 1;
                    BuildingAdapter buildingAdapter = new BuildingAdapter(getView().getActivity(), buildings);
                    getView().getBuildingSpinner().setAdapter(buildingAdapter);
                    getView().getBuildingSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Building buildingObject = buildings.get(position);
                            buildingId = buildingObject.getId();
                            buildingName = buildingObject.getName();
                            AppDataUtil.putLastSelectedBuilding(buildingId);
                            AppDataUtil.putLastSelectedBuildingName(buildingName);
                            // 更新场馆时刷新页面
                            getView().showLoading();
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
                getView().hideLoading();
                getView().showMessage(message);
            }
        });
    }

    /**
     * 获取一个场馆的所有楼层与大厅
     */
    @Override
    public void getRooms() {
        reserveModel.getBuildingStatus(buildingId, new BaseRequestCallback<List<Room>>() {
            @Override
            public void onSuccess(List<Room> data) {
                super.onSuccess(data);
                roomAdapter.updateData(data);
                getView().hideLoading();
                getView().getRoomLayout().setRefreshing(false);
                getView().getRoomLayout().setVisibility(View.VISIBLE);
                activity.setUiMode(MainActivity.ROOM_MODE);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().hideLoading();
                getView().showMessage(message);
            }
        });

    }

    /**
     * 获取一个楼层或大厅的所有座位状态
     */
    @Override
    public void getSeats() {
        reserveModel.getRoomStatus(roomId, date, new BaseRequestCallback<List<Seat>>() {
            @Override
            public void onSuccess(List<Seat> data, int[] layout) {
                super.onSuccess(data);
                // 当用户进入一个房间时记录下当前房间的布局和座位
                seatList = data;
                roomLayout = layout;
                // 由于用户使用时间筛选座位功能时，会重新设置adapter，此处需要重置adapter
                getView().getSeatView().setAdapter(seatAdapter);
                seatAdapter.updateData(data);
                getView().hideLoading();
                getView().getSeatLayout().setRefreshing(false);
                getView().getSeatLayout().setVisibility(View.VISIBLE);
                activity.setUiMode(MainActivity.SEAT_MODE);
                getView().getHorizontalScrollView().fullScroll(View.FOCUS_RIGHT);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().hideLoading();
                getView().showMessage(message);
            }
        });
    }

    /**
     * 获取一个座位可以使用的开始时间
     */
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

    /**
     * 获取一个座位可以使用的结束时间
     */
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

    /**
     * 对选择好的座位与时间向服务器发起预约
     */
    @Override
    public void reserve() {
        // 如果开始时间选择的是现在，则参数为-1
        if (startTime.equals("now")) {
            startTime = "-1";
        }
        reserveModel.reserveSeat(seatId, date, startTime, endTime, new BaseRequestCallback<InstantReserve>() {
            @Override
            public void onSuccess(InstantReserve data) {
                super.onSuccess(data);
                String onDate = data.getOnDate().replace(" ", "");
                String begin = data.getBegin().replace(" ", "");
                String end = data.getEnd().replace(" ", "");
                String location = data.getLocation().replace(" ", "");

                String detail = "座位详细信息：\n" + location + "\n" + onDate + "，" + begin + "-" + end;

                Alerter.create(activity)
                        .setTitle("已经成功为您预约座位~")
                        .setText(detail)
                        .setDuration(10000)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();

//                if (data.isCheckedIn()) {
//                    setCurrentReserve();
//                }

                setCurrentReserve();

                getView().hideLoading();
                getView().getSeatLayout().setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);

                // 预约出错时返回到显示座位列表的页面
                getView().hideLoading();
                getView().getSeatLayout().setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 根据用户所给的时间段筛选某个楼层或大厅可以使用的座位
     */
    @Override
    public void getSeatsByTime() {
        reserveModel.selectSeatByTime(buildingId, roomId, date, startTime, endTime, new BaseRequestCallback<List<Seat>>() {
            @Override
            public void onSuccess(List<Seat> data) {
                super.onSuccess(data);

                // TODO: 实现按时间筛选座位
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        });
    }

    /**
     * 停止使用当前的座位
     */
    @Override
    public void stopSeat() {
        if (currentReserveId != 0) {
            seatActionModel.stop(new BaseRequestCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    super.onSuccess(data);
                    getView().showMessage("停止使用座位成功");
                    // 停止使用后立即更新当前用户使用座位预约的状态
                    setCurrentReserve();
                    setFabLogin();
                }

                @Override
                public void onError(String message) {
                    super.onError(message);
                    getView().showMessage(message);
                }
            });
        }
    }

    /**
     * Fragment初始化以及刷新页面，重新设置RecyclerView的Adapter
     */
    @Override
    public void init() {
        // 这些操作均在Fragment初始化时完成
        final RecyclerView roomView = getView().getRoomView();
        final RecyclerView seatView = getView().getSeatView();
        final SeatLayoutView seatLayoutView = getView().getSeatLayoutView();

        // 下面代码分为四个部分：Room Adapter, Seat Adapter, Time Selector, Seat Layout Action

        // Room Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        roomAdapter = new RoomAdapter(getView().getActivity(), new ArrayList<Room>());
        roomAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Room room = roomAdapter.getRoomByPosition(position);
                roomId = room.getRoomId();
                roomName = room.getRoom();

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

        // Seat Adapter
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getView().getActivity(), 5);
        seatAdapter = new SeatAdapter(getView().getActivity(), new ArrayList<Seat>());
        seatAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final Seat seat = seatAdapter.getSeatByPosition(position);
                seatId = seat.getId();

                Log.d(TAG, "用户选中了座位：" + seat.getId() + "，座位号为：" + seat.getName());
                doUserReserve(seat);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                getView().showMessage("Seat Name:" + seatAdapter.getSeatByPosition(position).getName()
                        + ",Seat Id:" + seatId);
            }
        });
        seatView.setLayoutManager(layoutManager2);
        seatView.setAdapter(seatAdapter);
        seatView.setItemAnimator(new DefaultItemAnimator());

        // Time Selector
        LinearLayout timeSelectView = getView().getTimeSelectView();
        timeSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getUiMode() == MainActivity.SEAT_MODE) {
                    final TimeChooserDialog dialog = new TimeChooserDialog(activity);

                    final Spinner startTimeSpinner = dialog.getStartTime();
                    Spinner endTimeSpinner = dialog.getEndTime();
                    Button button = dialog.getButton();

                    final SeatTimeAdapter seatTimeAdapter1 = new SeatTimeAdapter(activity, AppDataUtil.getFullSeatTime());
                    final SeatTimeAdapter seatTimeAdapter2 = new SeatTimeAdapter(activity, AppDataUtil.getFullSeatTime());
                    startTimeSpinner.setAdapter(seatTimeAdapter1);
                    startTimeSpinner.setSelection(0);
                    endTimeSpinner.setAdapter(seatTimeAdapter2);
                    endTimeSpinner.setSelection(0);

                    startTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            startTime = seatTimeAdapter1.getSeatTimeByPosition(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    endTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            endTime = seatTimeAdapter2.getSeatTimeByPosition(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Integer.parseInt(startTime) > Integer.parseInt(endTime)) {
                                Toast.makeText(activity, "请选择合适的开始与结束时间", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(activity, "正在按照时间要求筛选座位", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getView().showLoading();
                            reserveModel.selectSeatByTime(buildingId, roomId, date, startTime, endTime, new BaseRequestCallback<List<Seat>>() {
                                @Override
                                public void onSuccess(final List<Seat> data) {
                                    super.onSuccess(data);
                                    getView().hideLoading();
                                    getView().getSeatLayout().setVisibility(View.VISIBLE);
                                    SeatAdapter adapter = new SeatAdapter(activity, data);
                                    adapter.setOnItemClickListener(new ClickListener() {
                                        @Override
                                        public void onItemClick(int position, View v) {
                                            Seat seat = data.get(position);
                                            seatId = seat.getId();

                                            Log.d(TAG, "用户选中了座位：" + seat.getId() + "，座位号为：" + seat.getName() + "\n"
                                                    + "时间段已选择，正在执行预约");

                                            getView().showLoading();
                                            reserve();
                                        }

                                        @Override
                                        public void onItemLongClick(int position, View v) {
                                            getView().showMessage("Seat Name:" + seatAdapter.getSeatByPosition(position).getName()
                                                    + ",Seat Id:" + seatId);
                                        }
                                    });
                                    getView().getSeatView().setAdapter(adapter);
                                }

                                @Override
                                public void onError(String message) {
                                    super.onError(message);
                                    getView().showMessage(message);
                                    getView().hideLoading();
                                }
                            });
                        }
                    });

                    dialog.show();
                } else if (activity.getUiMode() == MainActivity.ROOM_MODE) {
                    Toast.makeText(activity, "请先进入场馆的某楼层", Toast.LENGTH_LONG).show();
                } else if (activity.getUiMode() == MainActivity.SEAT_LAYOUT_MODE) {
                    Toast.makeText(activity, "目前布局选座无法时间筛选", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Seat Layout Action
        seatLayoutView.setClickListener(new SeatLayoutView.OnClickListener() {
            @Override
            public void onSeatClick(Seat seat) {
                seatId = seat.getId();

                Log.d(TAG, "用户选中了座位：" + seat.getId() + "，座位号为：" + seat.getName());
                doUserReserve(seat);
            }

            @Override
            public void onSeatLongClick(Seat seat) {
                getView().showMessage("Seat Name:" + seat.getName() + ",Seat Id:" + seat.getId());
            }
        });
        LinearLayout seatLayoutAction = getView().getLayoutActionView();
        seatLayoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getUiMode() == MainActivity.SEAT_MODE) {
                    // 进入布局选座模式
                    activity.setUiMode(MainActivity.SEAT_LAYOUT_MODE);
                    getView().showLoading();
                    getView().hideLoading();
                    getView().getSeatLayoutView().setVisibility(View.VISIBLE);

                    seatLayoutView.setDataAndRefresh(seatList, roomLayout);
                } else if (activity.getUiMode() == MainActivity.ROOM_MODE) {
                    Toast.makeText(activity, "请进入场馆的某楼层后再使用座位布局功能", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 当用户点击某个位置时，弹出对话框执行预约
     */
    private void doUserReserve(Seat seat){

        // 当用户选中一个座位，弹出对话框让用户选择开始时间和结束时间，并预约座位
        final SeatChooserDialog dialog = new SeatChooserDialog(activity);

        final Spinner startTimeSpinner = dialog.getStartTime();
        final Spinner endTimeSpinner = dialog.getEndTime();
        Button reserveButton = dialog.getButton();
        ImageView seatStar = dialog.getStar();

        final StaredSeat staredSeat = seat.toStaredSeat();
        staredSeat.setBuildingId(buildingId);
        staredSeat.setRoomId(roomId);
        staredSeat.setDetailLocation(buildingName + "\n" + roomName);
        final boolean isSeatStared = AppDataUtil.isSeatStared(staredSeat);
        if (isSeatStared) {
            // 如果座位被用户收藏，则显示为实心的小星星
            dialog.getStar().setImageResource(R.drawable.ic_action_star);
        }


        final SeatTimeAdapter seatTimeAdapter1 = new SeatTimeAdapter(activity, new ArrayList<SeatTime>());
        startTimeSpinner.setAdapter(seatTimeAdapter1);
        final SeatTimeAdapter seatTimeAdapter2 = new SeatTimeAdapter(activity, new ArrayList<SeatTime>());
        endTimeSpinner.setAdapter(seatTimeAdapter2);
        startTimeSpinner.setEnabled(false);
        endTimeSpinner.setEnabled(false);

        reserveModel.getSeatStartTime(seatId, date, new BaseRequestCallback<List<SeatTime>>() {
            @Override
            public void onSuccess(List<SeatTime> data) {
                super.onSuccess(data);
                if (data.size() == 0) {
                    getView().showMessage("无可用时间");
                    return;
                }
                startTimeSpinner.setEnabled(true);
                seatTimeAdapter1.updateData(data);

                startTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SeatTime seatTime = seatTimeAdapter1.getSeatTimeByPosition(position);
                        startTime = seatTime.getId();

                        endTimeSpinner.setEnabled(false);
                        reserveModel.getSeatEndTime(seatId, date, startTime, new BaseRequestCallback<List<SeatTime>>() {
                            @Override
                            public void onSuccess(List<SeatTime> data) {
                                super.onSuccess(data);
                                seatTimeAdapter2.updateData(data);
                                endTimeSpinner.setEnabled(true);

                                endTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        endTime = seatTimeAdapter2.getSeatTimeByPosition(position).getId();

                                        // 到此处已经完成了预约一个座位所需的所有参数，当此时用户点击预约按钮时，向系统发起预约
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }

                            @Override
                            public void onError(String message) {
                                super.onError(message);
                                getView().showMessage(message);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                getView().showMessage(message);
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((startTime != null) && (endTime != null)) {
                    // 点击预约按钮后发起预约，对话框消失并且页面显示为正在加载
                    reserve();
                    dialog.dismiss();
                    getView().showLoading();
                }
            }
        });

        seatStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSeatStared) {
                    dialog.getStar().setImageResource(R.drawable.ic_action_star_border);
                    AppDataUtil.setSeatStared(staredSeat, false);
                } else {
                    dialog.getStar().setImageResource(R.drawable.ic_action_star);
                    AppDataUtil.setSeatStared(staredSeat, true);
                }
            }
        });

        // 初始化完成对话框后展示出来
        dialog.show();
    }

    /**
     * 设置页面右下角的fab的状态为停止使用座位
     */
    private void setFabStopUsing() {
        // 设置主页面上的FAB为停止使用的状态
        if (activity != null) {
            activity.setFabFunction(MainActivity.FAB_STOP_SEAT);
        }
    }

    /**
     * 设置页面右下角的fab的状态为刷新token
     */
    private void setFabLogin() {
        // 设置主页面上的FAB为登录按钮的状态
        if (activity != null) {
            activity.setFabFunction(MainActivity.FAB_TO_LOGIN);
        }
    }
}
