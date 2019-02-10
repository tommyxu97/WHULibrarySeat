package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseFragment;
import com.xht97.whulibraryseat.contract.ReserveContract;
import com.xht97.whulibraryseat.presenter.ReservePresenter;


public class ReserveFragment extends BaseFragment<ReserveFragment, ReservePresenter> implements ReserveContract.IReserveView {

    private ProgressBar progressBar;

    private LinearLayout bar;
    private Spinner dateSpinner;
    private Spinner buildingSpinner;

    private RecyclerView roomView;
    private RecyclerView seatView;

    private TextView statusTitle;
    private TextView statusDetail;

    public ReserveFragment() {
    }

    @Override
    protected void initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reserve, container, false);

        progressBar = mRootView.findViewById(R.id.pb_reserve);
        bar = mRootView.findViewById(R.id.ll_reserve_bar);
        dateSpinner = mRootView.findViewById(R.id.sp_date);
        buildingSpinner = mRootView.findViewById(R.id.sp_building);
        roomView = mRootView.findViewById(R.id.rv_reserve_room);
        seatView = mRootView.findViewById(R.id.rv_reserve_seat);
        statusTitle = mRootView.findViewById(R.id.tv_reserve_status_title);
        statusDetail = mRootView.findViewById(R.id.tv_reserve_status_detail);

    }

    @Override
    protected void initData() {
        // 初始化页面上的数据
        // 首先显示为正在加载数据，并在加载完成时调用hideLoading
        showLoading();
        mPresenter.setAdapter();

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

        roomView.setVisibility(View.INVISIBLE);
        seatView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

    }

    public ProgressBar getProgressBar() {
        return progressBar;
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

    public RecyclerView getRoomView() {
        return roomView;
    }

    public RecyclerView getSeatView() {
        return seatView;
    }

    public TextView getStatusTitle() {
        return statusTitle;
    }

    public TextView getStatusDetail() {
        return statusDetail;
    }
}