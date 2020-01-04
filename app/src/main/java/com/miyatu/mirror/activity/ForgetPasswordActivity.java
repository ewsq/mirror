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
import com.miyatu.mirror.bean.ForgetPasswordBean;
import com.miyatu.mirror.bean.SmsBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends PublicActivity implements HttpOnNextListener {
    private EditText et_phonenumber=null,et_numbercode=null, et_password = null;
    private TextView tv_req_nunbercode=null;
    private Button btn_reset=null;
    private ImageView iv_registe_back=null;

    private MyCountDownTimer myCountDownTimer;

    private SmsBean.DataBean smsBean;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initSendSmsRequest(String mobile) {
        params = new HashMap<>();
        params.put("type", 2);
        params.put("mobile", mobile);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.SEND_SMS);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initResetRequest(String mobile, String code, String sessionId, String password) {
        params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("session_id", sessionId);
        params.put("password", password);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.FORGET_PASSWORD);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {
        et_phonenumber=findViewById(R.id.et_phonenumber);
        et_password = findViewById(R.id.et_password);
        et_numbercode=findViewById(R.id.et_numbercode);
        tv_req_nunbercode=findViewById(R.id.tv_req_nunbercode);
        btn_reset=findViewById(R.id.btn_reset);
        iv_registe_back=findViewById(R.id.iv_registe_back);
    }

    @Override
    protected void initData() {
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        myCountDownTimer = new MyCountDownTimer(30000,1000);
    }

    @Override
    protected void initEvent() {
        iv_registe_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_req_nunbercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phonenumber.getText().toString().trim() == null || et_phonenumber.getText().toString().trim().equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                myCountDownTimer.start();
                initSendSmsRequest(et_phonenumber.getText().toString().trim());
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = et_phonenumber.getText().toString().trim();
                String code = et_numbercode.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (phoneNumber == null || phoneNumber.equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                if (code == null || code.equals("")) {
                    ToastUtils.show("验证码不能为空");
                    return;
                }
                if (password.trim() == null || password.trim().equals("")) {
                    ToastUtils.show("密码不能为空");
                    return;
                }
                initResetRequest(phoneNumber, code, smsBean.getSession_id(), password);
            }
        });
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.FORGET_PASSWORD)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            ForgetPasswordBean data = new Gson().fromJson(resulte, new TypeToken<ForgetPasswordBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                ToastUtils.show("重置成功");
                finish();
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.SEND_SMS)) {
            LogUtils.i(resulte);
            SmsBean data = new Gson().fromJson(resulte, new TypeToken<SmsBean>(){}.getType());
            if (data.getStatus() == 1) {
                smsBean = data.getData();
                return;
            }
        }
    }

    @Override
    public void onError(ApiException e) {

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
            tv_req_nunbercode.setClickable(false);
            tv_req_nunbercode.setText(l/1000 + "");
        }
        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tv_req_nunbercode.setText("重新获取");
            //设置可点击
            tv_req_nunbercode.setClickable(true);
        }
    }
}
