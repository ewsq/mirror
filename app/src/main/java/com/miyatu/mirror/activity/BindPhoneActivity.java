package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.SmsBean;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class BindPhoneActivity extends PublicActivity implements HttpOnNextListener {
    private String openID;
    private String sessionID = "";

    private EditText etPhone;           //手机号
    private EditText etCode;            //验证码
    private TextView tvReqCode;         //获取验证码
    private Button btnBind;             //绑定
    private ImageView iv_registe_back;

    private MyCountDownTimer myCountDownTimer;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initBindRequest(String mobile, String code, String sessionID, String openID) {
        params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("session_id", sessionID);
        params.put("open_id", openID);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.BIND_PHONE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initSendSmsRequest(String mobile) {
        params = new HashMap<>();
        params.put("type", 1);
        params.put("mobile", mobile);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.SEND_SMS);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context, String openID){
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra("openID", openID);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView() {
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        myCountDownTimer = new MyCountDownTimer(30000,1000);

        etPhone = findViewById(R.id.et_phonenumber);
        etCode = findViewById(R.id.et_numbercode);
        tvReqCode = findViewById(R.id.tv_req_nunbercode);
        btnBind = findViewById(R.id.btn_bind);
        iv_registe_back = findViewById(R.id.iv_registe_back);
    }

    @Override
    protected void initData() {
        openID = getIntent().getStringExtra("openID");
    }

    @Override
    protected void initEvent() {
        tvReqCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString().trim() == null || etPhone.getText().toString().trim().equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                myCountDownTimer.start();
                initSendSmsRequest(etPhone.getText().toString().trim());
            }
        });
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etPhone.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (sessionID.equals("")) {
                    ToastUtils.show("请先获取验证码");
                    return;
                }
                if (mobile == null || mobile.equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                if (code == null || code.equals("")) {
                    ToastUtils.show("验证码不能为空");
                    return;
                }
                initBindRequest(mobile, code, sessionID, openID);
            }
        });
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.SEND_SMS)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            SmsBean data = new Gson().fromJson(resulte, new TypeToken<SmsBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                sessionID = data.getData().getSession_id();
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.BIND_PHONE)) {
            UserDatabean data = new Gson().fromJson(resulte, new TypeToken<UserDatabean>(){}.getType());
            PreferencesUtils.putString(BindPhoneActivity.this, PreferencesUtils.USERDATA, new Gson().toJson(data.getData()));
            EventBus.getDefault().post(data.getData());
            EventBus.getDefault().post(CommonUtils.REFRESH);
            finish();
            return;
        }
    }

    @Override
    public void onError(ApiException e) {
        ToastUtils.show(e.getMessage());
    }

    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tvReqCode.setClickable(false);
            tvReqCode.setText(l/1000 + "");
        }
        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tvReqCode.setText("重新获取");
            //设置可点击
            tvReqCode.setClickable(true);
        }
    }
}
