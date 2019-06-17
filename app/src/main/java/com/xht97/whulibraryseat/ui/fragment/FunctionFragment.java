package com.xht97.whulibraryseat.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseFragment;
import com.xht97.whulibraryseat.presenter.FunctionPresenter;
import com.xht97.whulibraryseat.ui.activity.functions.StaredSeatActivity;

public class FunctionFragment extends BaseFragment<FunctionFragment, FunctionPresenter> {

    private FunctionFragment fragment = this;

    public FunctionFragment() {

    }

    @Override
    protected void initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_function, container, false);

        LinearLayout staredSeatView = mRootView.findViewById(R.id.ll_function_stared_seats);

        staredSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment.getActivity(), StaredSeatActivity.class);
                fragment.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected FunctionPresenter createPresenter() {
        return new FunctionPresenter();
    }
}
