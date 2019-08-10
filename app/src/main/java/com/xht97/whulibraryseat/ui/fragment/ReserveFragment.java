package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseFragment;
import com.xht97.whulibraryseat.contract.ReserveContract;
import com.xht97.whulibraryseat.presenter.ReservePresenter;
import com.xht97.whulibraryseat.ui.weight.SeatLayoutView;


public class ReserveFragment extends BaseFragment<ReserveFragment, ReservePresenter> implements ReserveContract.IReserveView {

    private ProgressBar progressBar;

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout bar;
    private Spinner dateSpinner;
    private Spinner buildingSpinner;

    private LinearLayout timeSelectView;
    private LinearLayout layoutActionView;

    private SwipeRefreshLayout roomLayout;
    private SwipeRefreshLayout seatLayout;
    private RecyclerView roomView;
    private RecyclerView seatView;
    private SeatLayoutView seatLayoutView;

    private TextView statusTitle;
    private TextView statusDetail;

    public ReserveFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setCurrentReserve();
    }

    @Override
    protected void initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reserve, container, false);

        progressBar = mRootView.findViewById(R.id.pb_reserve);
        horizontalScrollView = mRootView.findViewById(R.id.hsv_reserve_bar);
        bar = mRootView.findViewById(R.id.ll_reserve_bar);
        dateSpinner = mRootView.findViewById(R.id.sp_date);
        buildingSpinner = mRootView.findViewById(R.id.sp_building);
        timeSelectView = mRootView.findViewById(R.id.ll_time_select);
        layoutActionView = mRootView.findViewById(R.id.ll_layout_action);
        roomLayout = mRootView.findViewById(R.id.sfl_reserve_room);
        seatLayout = mRootView.findViewById(R.id.sfl_reserve_seat);
        roomView = mRootView.findViewById(R.id.rv_reserve_room);
        seatView = mRootView.findViewById(R.id.rv_reserve_seat);
        seatLayoutView = mRootView.findViewById(R.id.slv_main);
        statusTitle = mRootView.findViewById(R.id.tv_reserve_status_title);
        statusDetail = mRootView.findViewById(R.id.tv_reserve_status_detail);

        roomLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        seatLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        roomLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getRooms();
            }
        });
        seatLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSeats();
            }
        });
    }

    @Override
    protected void initData() {
        // 初始化页面上的数据
        // 首先显示为正在加载数据，并在加载完成时调用hideLoading
        showLoading();
        mPresenter.init();

        mPresenter.setCurrentReserve();
        mPresenter.setAvailableTime();

    }

    @Override
    protected ReservePresenter createPresenter() {
        return new ReservePresenter();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

        roomLayout.setVisibility(View.INVISIBLE);
        seatLayout.setVisibility(View.INVISIBLE);
        seatLayoutView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void stopSeat() {
        mPresenter.stopSeat();
    }

    @Override
    public void setRoomMode() {
        roomLayout.setVisibility(View.VISIBLE);
        seatLayout.setVisibility(View.INVISIBLE);
        seatLayoutView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateCurrentReserve() {
        mPresenter.setCurrentReserve();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public HorizontalScrollView getHorizontalScrollView() {
        return horizontalScrollView;
    }

    public LinearLayout getBar() {
        return bar;
    }

    public Spinner getDateSpinner() {
        return dateSpinner;
    }

    public Spinner getBuildingSpinner() {
        return buildingSpinner;
    }

    public LinearLayout getTimeSelectView() {
        return timeSelectView;
    }

    public LinearLayout getLayoutActionView() {
        return layoutActionView;
    }

    public SwipeRefreshLayout getRoomLayout() {
        return roomLayout;
    }

    public SwipeRefreshLayout getSeatLayout() {
        return seatLayout;
    }

    public RecyclerView getRoomView() {
        return roomView;
    }

    public RecyclerView getSeatView() {
        return seatView;
    }

    public SeatLayoutView getSeatLayoutView() {
        return seatLayoutView;
    }

    public TextView getStatusTitle() {
        return statusTitle;
    }

    public TextView getStatusDetail() {
        return statusDetail;
    }
}
