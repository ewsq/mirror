package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;

public class ConnectServiceActivity extends PublicActivity {
    private ImageView iv_back;
    private TextView tvPhoneNumber;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ConnectServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_service;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhoneNumber.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void updateView() {

    }
}
