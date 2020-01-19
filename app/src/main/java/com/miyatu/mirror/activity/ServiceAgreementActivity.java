package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;

public class ServiceAgreementActivity extends PublicActivity {
    private RelativeLayout rlBack;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ServiceAgreementActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_agreement;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void updateView() {

    }
}
