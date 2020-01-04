package com.miyatu.mirror.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.SmsBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册 电话号码 、验证码页面
 */
public class RegisteActivity extends RxAppCompatActivity implements View.OnClickListener, HttpOnNextListener {
    private EditText et_phonenumber=null,et_numbercode=null, et_password = null;
    private TextView tv_req_nunbercode=null;
    private Button btn_registe_nextstep=null;
    private ImageView iv_registe_back=null;

    private MyCountDownTimer myCountDownTimer;

    private SmsBean.DataBean smsBean;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest(String mobile) {
        params = new HashMap<>();
        params.put("type", 1);
        params.put("mobile", mobile);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.SEND_SMS);
        api.setParams(params);
        manager.doHttpDeal(api);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);

        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        myCountDownTimer = new MyCountDownTimer(30000,1000);

        initView();
    }

    private void initView() {
        et_phonenumber=findViewById(R.id.et_phonenumber);
        et_phonenumber.setOnClickListener(this);
        et_password = findViewById(R.id.et_password);
        et_password.setOnClickListener(this);
        et_numbercode=findViewById(R.id.et_numbercode);
        et_numbercode.setOnClickListener(this);
        tv_req_nunbercode=findViewById(R.id.tv_req_nunbercode);
        tv_req_nunbercode.setOnClickListener(this);
        btn_registe_nextstep=findViewById(R.id.btn_registe_nextstep);
        btn_registe_nextstep.setOnClickListener(this);
        iv_registe_back=findViewById(R.id.iv_registe_back);
        iv_registe_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_registe_back:{
                finish();
            }
            case R.id.tv_req_nunbercode:{
                if (et_phonenumber.getText().toString().trim() == null || et_phonenumber.getText().toString().trim().equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                myCountDownTimer.start();
                initRequest(et_phonenumber.getText().toString().trim());
                break;
            }
            case R.id.btn_registe_nextstep:{
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
                Intent intent=new Intent(RegisteActivity.this,RegisteInfoActivity.class);
                intent.putExtra("mobile", phoneNumber);
                intent.putExtra("code", Integer.parseInt(code));
                intent.putExtra("password", password);
                intent.putExtra("session_id", smsBean.getSession_id());
                startActivity(intent);
                break;

            }
        }
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.SEND_SMS)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            SmsBean data = new Gson().fromJson(resulte, new TypeToken<SmsBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                smsBean = data.getData();

                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.i(e.getMessage());
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
