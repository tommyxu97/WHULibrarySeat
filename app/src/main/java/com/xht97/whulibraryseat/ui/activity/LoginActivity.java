package com.xht97.whulibraryseat.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xht97.whulibraryseat.R;
import com.xht97.whulibraryseat.base.BaseActivity;
import com.xht97.whulibraryseat.contract.LoginContract;
import com.xht97.whulibraryseat.presenter.LoginPresenter;


public class LoginActivity extends BaseActivity<LoginActivity, LoginPresenter> implements LoginContract.ILoginView{

    public static int STUDENT_ID_INPUT = 0;
    public static int PASSWORD_INPUT = 1;

    private EditText studentId;
    private EditText password;
    private CheckBox checkBox;
    private Button loginButton;
    private TextView userAgreement;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void initView() {
        // 初始化所有界面上的控件
        setContentView(R.layout.activity_login);

        studentId = findViewById(R.id.et_login_studentId);
        password = findViewById(R.id.et_login_password);
        checkBox = findViewById(R.id.cb_login);
        loginButton = findViewById(R.id.bt_login_action);
        userAgreement = findViewById(R.id.tv_login_agreement);
        progressBar = findViewById(R.id.pb_login);
        linearLayout = findViewById(R.id.ll_login);

        // 页面初始化时根据本地存储的用户偏好自动给checkbox打钩
        checkBox.setChecked(mPresenter.getAutoLogin());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setAutoLogin(isChecked);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(getUserId(), getPassword());
            }
        });
    }

    @Override
    protected void initData() {
        // 如果本地有存储密码，直接填充好
        mPresenter.fillPassword();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading() {
        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        // 本页面不存在空View显示情况
    }

    @Override
    public String getUserId() {
        return studentId.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void showErrorFocus(int which, String message) {
        switch (which) {
            // 0表示学号输入框
            case 0:
                studentId.requestFocus();
                studentId.setError(message);
                break;
            // 1表示密码
            case 1:
                password.requestFocus();
                password.setError(message);
                break;
        }
    }

    public EditText getStudentId() {
        return studentId;
    }

    public EditText getPasswordView() {
        return password;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public TextView getUserAgreement() {
        return userAgreement;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }
}