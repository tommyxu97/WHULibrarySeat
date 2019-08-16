package com.xht97.whulibraryseat.ui.activity.functions;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;
import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.base.BaseView;
import com.xht97.whulibraryseat.model.IReserveModel;
import com.xht97.whulibraryseat.model.bean.Building;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.model.bean.StaredSeat;
import com.xht97.whulibraryseat.model.impl.ReserveModelImpl;
import com.xht97.whulibraryseat.ui.adapter.DateAdapter;
import com.xht97.whulibraryseat.ui.adapter.SeatTimeAdapter;
import com.xht97.whulibraryseat.ui.adapter.StaredSeatAdapter;
import com.xht97.whulibraryseat.ui.listener.ClickListener;
import com.xht97.whulibraryseat.ui.weight.SeatChooserDialog;
import com.xht97.whulibraryseat.util.AppDataUtil;

import java.util.ArrayList;
import java.util.List;

public class StaredSeatActivity extends AppCompatActivity implements BaseView {

    private StaredSeatActivity activity = this;

    private Spinner dateSpinner;
    private RecyclerView recyclerView;

    private IReserveModel reserveModel = new ReserveModelImpl();
    private List<StaredSeat> staredSeats = new ArrayList<>();

    private String date;
    private String startTime;
    private String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stared_seats);

        initView();

        initData();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dateSpinner = findViewById(R.id.sp_stared_seats_date);
        recyclerView = findViewById(R.id.rv_stared_seat);
    }

    private void initData() {
        staredSeats = AppDataUtil.getStaredSeats();

        reserveModel.getAvailableDate(new BasePresenter.BaseRequestCallback<List<List>>() {
            @Override
            public void onSuccess(final List<List> lists) {
                super.onSuccess(lists);
                if (!lists.isEmpty()) {
                    final List<String> dates = lists.get(0);
                    final List<Building> buildings = lists.get(1);

                    // 设置可选的日期
                    DateAdapter dateAdapter = new DateAdapter(activity, dates);
                    dateSpinner.setAdapter(dateAdapter);
                    dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        dateSpinner.setSelection(1);
                    } else {
                        date = dates.get(0);
                    }
                }
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                activity.showMessage(message);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        final StaredSeatAdapter staredSeatAdapter = new StaredSeatAdapter(activity, staredSeats);
        staredSeatAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final StaredSeat staredSeat = staredSeatAdapter.getStaredSeatByPosition(position);

                final int seatId = staredSeat.getId();

                // 当用户选中一个座位，弹出对话框让用户选择开始时间和结束时间，并预约座位
                final SeatChooserDialog dialog = new SeatChooserDialog(activity);

                final Spinner startTimeSpinner = dialog.getStartTime();
                final Spinner endTimeSpinner = dialog.getEndTime();
                Button reserveButton = dialog.getButton();
                ImageView seatStar = dialog.getStar();

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

                reserveModel.getSeatStartTime(seatId, date, new BasePresenter.BaseRequestCallback<List<SeatTime>>() {
                    @Override
                    public void onSuccess(List<SeatTime> data) {
                        super.onSuccess(data);
                        if (data.size() == 0) {
                            activity.showMessage("无可用时间");
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
                                reserveModel.getSeatEndTime(seatId, date, startTime, new BasePresenter.BaseRequestCallback<List<SeatTime>>() {
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
                                        activity.showMessage(message);
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
                        activity.showMessage(message);
                    }
                });

                reserveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((startTime != null) && (endTime != null)) {
                            // 点击预约按钮后发起预约，对话框消失并且页面显示为正在加载
                            reserveModel.reserveSeat(seatId, date, startTime, endTime, new BasePresenter.BaseRequestCallback<InstantReserve>() {
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
                                }

                                @Override
                                public void onError(String message) {
                                    super.onError(message);
                                    activity.showMessage(message);
                                }
                            });
                            dialog.dismiss();
                            activity.showLoading();
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

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(staredSeatAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
