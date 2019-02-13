package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.xht97.whulibraryseat.ui.adapter.ReserveHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReserveHistoryFragment extends Fragment {

    private UserInfoModelImpl userInfoModel = new UserInfoModelImpl();
    private final ReserveModelImpl reserveModel = new ReserveModelImpl();

    private ReserveHistoryAdapter adapter;

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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReserveHistoryAdapter(getActivity(), new ArrayList<ReserveHistory>());
        adapter.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReserveHistory history = (ReserveHistory) v.getTag();
                reserveModel.cancelSeat(history.getId(), new BasePresenter.BaseRequestCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        super.onSuccess(data);
                        Toast.makeText(getContext(), "取消作为预约成功", Toast.LENGTH_SHORT).show();
                        initData();
                    }

                    @Override
                    public void onError(String message) {
                        super.onError(message);
                        Toast.makeText(getContext(), "取消预约失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        userInfoModel.getReserveHistory(new BasePresenter.BaseRequestCallback<List<ReserveHistory>>() {
            @Override
            public void onSuccess(List<ReserveHistory> data) {
                super.onSuccess(data);
                adapter.updateData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                Toast.makeText(getContext(), "获取用户历史预约记录失败", Toast.LENGTH_SHORT).show();
            }
        });

        initData();


        return rootView;
    }

    private void initData() {
        userInfoModel.getReserveHistory(new BasePresenter.BaseRequestCallback<List<ReserveHistory>>() {
            @Override
            public void onSuccess(List<ReserveHistory> data) {
                super.onSuccess(data);
                adapter.updateData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                Toast.makeText(getContext(), "获取用户历史预约记录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
