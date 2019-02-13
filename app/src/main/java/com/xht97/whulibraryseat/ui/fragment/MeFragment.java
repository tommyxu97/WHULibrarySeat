package com.xht97.whulibraryseat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseFragment;
import com.xht97.whulibraryseat.contract.MeContract;
import com.xht97.whulibraryseat.presenter.MePresenter;
import com.xht97.whulibraryseat.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends BaseFragment<MeFragment, MePresenter> implements MeContract.IMeView{

    private ImageView logoView;
    private TextView userNameView;
    private TextView userIdView;
    private TextView userStatusView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public MeFragment() {
    }

    @Override
    protected void initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_me, container, false);

        logoView = mRootView.findViewById(R.id.iv_me_user_logo);
        userNameView = mRootView.findViewById(R.id.tv_me_user_name);
        userIdView = mRootView.findViewById(R.id.tv_me_user_id);
        userStatusView = mRootView.findViewById(R.id.tv_me_user_status);

        tabLayout = mRootView.findViewById(R.id.tl_me);
        viewPager = mRootView.findViewById(R.id.vp_me);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ReserveHistoryFragment());
        fragments.add(new ViolationHistoryFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        List<String> titleList = new ArrayList<>();
        titleList.add("预约记录");
        titleList.add("违约历史");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)), true);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
    }

    @Override
    protected void initData() {
        mPresenter.updateUserData();
    }

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyView() {

    }

    public ImageView getLogoView() {
        return logoView;
    }

    public TextView getUserNameView() {
        return userNameView;
    }

    public TextView getUserIdView() {
        return userIdView;
    }

    public TextView getUserStatusView() {
        return userStatusView;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
