package com.xht97.whulibraryseat.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.util.AppDataUtil;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView userIdView = findViewById(R.id.tv_setting_user_id);
        MaterialButton clearButton = findViewById(R.id.bt_setting_user_info);

        String userId = AppDataUtil.getMainId();
        userIdView.setText("UserId: " + (userId.equals("") ? "未登录" : userId));
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("提示")
                        .setMessage("确认清除账号数据嘛")
                        .setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppDataUtil.putMainId("");
                                AppDataUtil.putMainPassword("");
                                Toast.makeText(SettingActivity.this, "已经删除本地账号数据，请退出重新登录", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
