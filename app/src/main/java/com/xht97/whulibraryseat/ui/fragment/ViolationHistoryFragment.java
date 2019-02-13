package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BasePresenter;
import com.xht97.whulibraryseat.model.bean.User;
import com.xht97.whulibraryseat.model.impl.UserInfoModelImpl;

public class ViolationHistoryFragment extends Fragment {

    /**
     * 此Fragment不使用包含Presenter的MVP模式
     */
    public ViolationHistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_violation_history, container, false);

        // 图书馆移动端API接口暂时没有提供违约具体信息的接口，因此先显示为违约次数
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_violation_history);

        final TextView violationNumberView = rootView.findViewById(R.id.tv_violation_number);

        UserInfoModelImpl userInfoModel = new UserInfoModelImpl();
        userInfoModel.getUserInfo(new BasePresenter.BaseRequestCallback<User>() {
            @Override
            public void onSuccess(User data) {
                super.onSuccess(data);
                violationNumberView.setText("违约次数为：" + String.valueOf(data.getViolationCount()));
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        });

        return rootView;
    }

}
