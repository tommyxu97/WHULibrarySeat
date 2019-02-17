package com.xht97.whulibraryseat.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
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

    /**
     * 用于实现切换FAB功能
     */
    public static final int FAB_TO_LOGIN = 1;
    public static final int FAB_STOP_SEAT = 2;

    /**
     * 用于记录目前用户使用页面的状态
     */
    public static final int COMMON_MODE = 3;
    public static final int ROOM_MODE = 4;
    public static final int SEAT_MODE = 5;

    Fragment currentFragment;
    ReserveFragment reserveFragment;
    FunctionFragment functionFragment;
    MeFragment meFragment;

    BottomNavigationView navigation;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    private int uiMode = COMMON_MODE;
    private long firstTime;

    private boolean isLogin = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        floatingActionButton = findViewById(R.id.fab_main);
        progressBar = findViewById(R.id.pb_main);

        floatingActionButton.setOnClickListener(new FabToLoginListener());

        // 如果用户没有登录，则直接跳转登录页(例如用户第一次打开软件，直接不初始化碎片并显示空页面)
        if (!(AppDataUtil.isAutoLogin() && AppDataUtil.isPasswordExists())) {
            isLogin = false;
            showEmptyView();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, StaticVar.RESULT_LOGIN);
            return;
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    }

    @Override
    protected void initData() {
        // 每次启动程序时，更新本地存储的token

        if (!isLogin) {
            return;
        }
        mPresenter.updateToken();
        // initFragment();
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
    public void setFabFunction(int type) {
        switch (type) {
            case FAB_TO_LOGIN:
                floatingActionButton.setImageResource(R.drawable.ic_action_login);
                floatingActionButton.setOnClickListener(new FabToLoginListener());
                break;
            case FAB_STOP_SEAT:
                floatingActionButton.setImageResource(R.drawable.ic_action_stop);
                floatingActionButton.setOnClickListener(new FabStopUsingSeatListener());
                break;
        }
    }

    @Override
    public void setUiMode(int mode) {
        uiMode = mode;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                // 如果用户在选择座位的界面上按返回键，则直接返回选择大厅的界面
                if (uiMode == SEAT_MODE) {
                    reserveFragment.setRoomMode();
                    uiMode = ROOM_MODE;
                    return true;
                }

                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    // 此处调用 System.exit(0); 也可实现退出，但是没有动画效果
                    this.finish();
                }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case StaticVar.RESULT_LOGIN:
                if (resultCode == RESULT_OK) {
                    isLogin = true;
                    // 如果开启应用时直接跳转了登录页面，则需要在返回时判断并初始化三个碎片
                    if (reserveFragment == null) {
                        initFragment();
                    }
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "暂时未完成", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化三个碎片
     */
    public void initFragment() {
        reserveFragment = new ReserveFragment();
        functionFragment = new FunctionFragment();
        meFragment = new MeFragment();
        switchFragment(reserveFragment).commitAllowingStateLoss();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(reserveFragment).commit();
                        if (floatingActionButton.getVisibility() == View.INVISIBLE) {
                            floatingActionButton.show();
                        }
                        return true;
                    case R.id.navigation_dashboard:
                        switchFragment(functionFragment).commit();
                        if (floatingActionButton.getVisibility() == View.INVISIBLE) {
                            floatingActionButton.show();
                        }
                        return true;
                    case R.id.navigation_notifications:
                        switchFragment(meFragment).commit();
                        if (floatingActionButton.getVisibility() == View.VISIBLE) {
                            floatingActionButton.hide();
                        }
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

    public ReserveFragment getReserveFragment() {
        return reserveFragment;
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

    /**
     * 实现ReserveFragment里存在当前正在使用的预约时，改变MainActivity中FAB的Action
     */
    private class FabToLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private class FabStopUsingSeatListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            reserveFragment.stopSeat();
        }
    }
}
