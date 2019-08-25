package com.xht97.whulibraryseat.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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

    private static final String TAG = "MainActivity";

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
    public static final int SEAT_LAYOUT_MODE = 6;

    MainActivity activity = this;
    FrameLayout emptyView;

    /**
     * 主页面的三个Fragment
     */
    private int currentFragment = 0;
    ReserveFragment reserveFragment;
    FunctionFragment functionFragment;
    MeFragment meFragment;

    BottomNavigationView navigation;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    /**
     * 表示预约界面的UI状态，实现用户点击返回键时对ReserveFragment的界面进行修改
     */
    private int uiMode = COMMON_MODE;

    /**
     * 记录用户第一次点击返回键的时间，用于实现双击退出程序
     */
    private long firstTime;

    /**
     * 表示用户是否已经登录
     */
    private boolean isLogin = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        floatingActionButton = findViewById(R.id.fab_main);
        progressBar = findViewById(R.id.pb_main);
        emptyView = findViewById(R.id.fl_main_empty);

        floatingActionButton.setOnClickListener(new FabToLoginListener());
        // 初始设置空白页不可见
        emptyView.setVisibility(View.INVISIBLE);

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
        // 正常情况下启动程序时，更新本地存储的token
        if (!isLogin) {
            return;
        }
        mPresenter.updateToken();
        //initFragment();
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
        // 展示空白页，并且当用户点击页面时会重新刷新
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
                initData();
            }
        });
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
                // 用户进入布局选座后，按下返回键返回顺序布局
                if (uiMode == SEAT_LAYOUT_MODE) {
                    reserveFragment.setSeatMode();
                    uiMode = SEAT_MODE;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate savedInstanceState is not null");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "-> onSaveInstanceState");
        outState.putInt("currentFragment", currentFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "-> onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        reserveFragment = (ReserveFragment) fragmentManager.findFragmentByTag("ReserveFragment");
        functionFragment = (FunctionFragment) fragmentManager.findFragmentByTag("FunctionFragment");
        meFragment = (MeFragment) fragmentManager.findFragmentByTag("MeFragment");
        switchFragment(savedInstanceState.getInt("currentFragment"));

        initNavigationListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case StaticVar.RESULT_LOGIN:
                if (resultCode == RESULT_OK) {
                    isLogin = true;
                    emptyView.setVisibility(View.INVISIBLE);
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
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化三个Fragment
     */
    public void initFragment() {
        reserveFragment = new ReserveFragment();
        functionFragment = new FunctionFragment();
        meFragment = new MeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_main, reserveFragment, "ReserveFragment");
        fragmentTransaction.add(R.id.fl_main, functionFragment, "FunctionFragment");
        fragmentTransaction.add(R.id.fl_main, meFragment, "MeFragment");
        fragmentTransaction.commit();

        switchFragment(0).commitAllowingStateLoss();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        initNavigationListener();
    }

    /**
     * 初始化底部导航栏监听器，需要在初始话Fragment后完成
     */
    private void initNavigationListener() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        switchFragment(0).commit();
                        floatingActionButton.show();
                        return true;
                    case R.id.navigation_functions:
                        switchFragment(1).commit();
                        floatingActionButton.show();
                        return true;
                    case R.id.navigation_me:
                        switchFragment(2).commit();
                        floatingActionButton.hide();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 切换主页碎片
     * @param fragmentId 传入的参数为fragment编号，取值范围[0,2]
     */
    private FragmentTransaction switchFragment(int fragmentId) {
        currentFragment = fragmentId;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentId) {
            case 0:
                fragmentTransaction.show(reserveFragment);
                fragmentTransaction.hide(functionFragment);
                fragmentTransaction.hide(meFragment);
                break;
            case 1:
                fragmentTransaction.hide(reserveFragment);
                fragmentTransaction.show(functionFragment);
                fragmentTransaction.hide(meFragment);
                break;
            case 2:
                fragmentTransaction.hide(reserveFragment);
                fragmentTransaction.hide(functionFragment);
                fragmentTransaction.show(meFragment);
                break;
        }
        return fragmentTransaction;
    }

    /**
     * 返回ReserveFragment对象
     */
    public ReserveFragment getReserveFragment() {
        return reserveFragment;
    }

    /**
     * 设置预约界面的UI_MODE
     */
    @Override
    public void setUiMode(int mode) {
        uiMode = mode;
    }

    /**
     * 获取预约界面的UI_MODE
     */
    @Override
    public int getUiMode() {
        return uiMode;
    }

    /**
     * 设置主页面上的悬浮按钮的功能
     * @param type FAB_TO_LOGIN与FAB_TO_SEAT 功能分别为跳转至登录页和停止使用座位
     */
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

    /**
     * 悬浮按钮跳转至登录页的监听器
     */
    private class FabToLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 悬浮按钮停止使用座位的监听器
     */
    private class FabStopUsingSeatListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("(ˉ▽￣～) ~")
                    .setMessage("确认释放当前所使用的座位吗？")
                    .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reserveFragment.stopSeat();
                        }
                    })
                    .create();
            dialog.show();
        }
    }
}
