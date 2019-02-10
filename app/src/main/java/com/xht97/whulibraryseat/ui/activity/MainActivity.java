package com.xht97.whulibraryseat.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseActivity;
import com.xht97.whulibraryseat.app.StaticVar;
import com.xht97.whulibraryseat.contract.MainContract;
import com.xht97.whulibraryseat.presenter.MainPresenter;
import com.xht97.whulibraryseat.ui.fragment.FunctionFragment;
import com.xht97.whulibraryseat.ui.fragment.MeFragment;
import com.xht97.whulibraryseat.ui.fragment.ReserveFragment;
import com.xht97.whulibraryseat.util.AppDataUtil;

public class MainActivity extends BaseActivity<MainActivity, MainPresenter> implements MainContract.IMainView{

    Fragment currentFragment;
    Fragment reserveFragment;
    Fragment functionFragment;
    Fragment meFragment;

    BottomNavigationView navigation;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        floatingActionButton = findViewById(R.id.fab_main);
        progressBar = findViewById(R.id.pb_main);

        // 如果用户没有登录，则直接跳转登录页
        if (!(AppDataUtil.isAutoLogin() && AppDataUtil.isPasswordExists())) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, StaticVar.RESULT_LOGIN);
            return;
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, StaticVar.RESULT_LOGIN);
            }
        });
    }

    @Override
    protected void initData() {
        // 每次启动程序时，更新本地存储的token

        //mPresenter.updateToken();
        initFragment();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case StaticVar.RESULT_LOGIN:
                if (resultCode == RESULT_OK) {
                    // 如果开启应用时直接跳转了登录页面，则需要在返回时判断并初始化三个碎片
                    if (reserveFragment == null) {
                        initFragment();
                    }
                    // TODO:通知三个碎片更新数据
                }
        }
    }

    /**
     * 初始化三个碎片
     */
    public void initFragment() {
        reserveFragment = new ReserveFragment();
        functionFragment = new FunctionFragment();
        meFragment = new MeFragment();
        switchFragment(reserveFragment).commit();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(reserveFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        switchFragment(functionFragment).commit();
                        return true;
                    case R.id.navigation_notifications:
                        switchFragment(meFragment).commit();
                        return true;
                }
                return false;
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(reserveFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        switchFragment(functionFragment).commit();
                        return true;
                    case R.id.navigation_notifications:
                        switchFragment(meFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 切换主页碎片
     */
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            // 程序初始化时currentFragment为空需要判断
            if (currentFragment!=null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.add(R.id.fl_main, targetFragment, targetFragment.getClass().getName());
        } else {
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(targetFragment);
        }
        currentFragment = targetFragment;
        return fragmentTransaction;
    }
}
