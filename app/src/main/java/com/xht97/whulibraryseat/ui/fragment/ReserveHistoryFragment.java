package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.impl.ReserveModelImpl;
import com.xht97.whulibraryseat.model.impl.UserInfoModelImpl;
import com.xht97.whulibraryseat.ui.activity.MainActivity;
import com.xht97.whulibraryseat.ui.adapter.ReserveHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReserveHistoryFragment extends Fragment {

    private UserInfoModelImpl userInfoModel = new UserInfoModelImpl();
    private final ReserveModelImpl reserveModel = new ReserveModelImpl();

    private ReserveHistoryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MainActivity activity;

    /**
     * 这个Fragment不使用包含Presenter的MVP模式
     */
    public ReserveHistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_reserve_history, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_reserve_history);
        swipeRefreshLayout = rootView.findViewById(R.id.sfl_reserve_history);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReserveHistoryAdapter(getActivity(), new ArrayList<ReserveHistory>());
        adapter.bindFragment(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        initData();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    public void initData() {
        userInfoModel.getReserveHistory(new BasePresenter.BaseRequestCallback<List<ReserveHistory>>() {
            @Override
            public void onSuccess(List<ReserveHistory> data) {
                super.onSuccess(data);
                adapter.updateData(data);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                Toast.makeText(getContext(), "获取用户历史预约记录失败", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public MainActivity getMainActivity() {
        return activity;
    }
}
