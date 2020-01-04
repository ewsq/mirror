package com.miyatu.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.hjq.toast.ToastUtils;

/**
 * 注册页面，姓名、邮箱
 */
public class RegisteInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_registeinfo_back = null;
    private EditText et_username = null, et_useremail = null;
    private Button btn_registeinfo_nextstep = null;

    private String mobile = "";
    private int code = 0;
    private String sessionId = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_info);
        initView();
    }

    private void initView() {
        iv_registeinfo_back = findViewById(R.id.iv_registeinfo_back);
        iv_registeinfo_back.setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
        et_username.setOnClickListener(this);
        et_useremail = findViewById(R.id.et_useremail);
        et_useremail.setOnClickListener(this);
        btn_registeinfo_nextstep = findViewById(R.id.btn_registeinfo_nextstep);
        btn_registeinfo_nextstep.setOnClickListener(this);

        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getIntExtra("code", 0);
        password = getIntent().getStringExtra("password");
        sessionId = getIntent().getStringExtra("session_id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_registeinfo_back: {
                finish();
            }
            case R.id.btn_registeinfo_nextstep: {
                String userName = et_username.getText().toString().trim();
                String userMail = et_useremail.getText().toString().trim();
                if (userName == null || userName.equals("")) {
                    ToastUtils.show("请输入用户名");
                    return;
                }
                if (userMail == null || userMail.equals("")) {
                    ToastUtils.show("请输入邮箱");
                    return;
                }
                Intent intent=new Intent(RegisteInfoActivity.this, RegisteSexbirthdayActivity.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("code", code);
                intent.putExtra("password", password);
                intent.putExtra("session_id", sessionId);
                intent.putExtra("nickname", userName);
                intent.putExtra("email", userMail);
                startActivity(intent);
                break;
            }
        }
    }
}
