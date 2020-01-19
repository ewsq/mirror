package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;

public class PrivacyPolicyActivity extends PublicActivity {
    private RelativeLayout rlBack;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PrivacyPolicyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_privacy_policy;
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
