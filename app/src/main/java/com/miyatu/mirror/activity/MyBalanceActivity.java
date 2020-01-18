package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.MyBalanceBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.ui.PayModeActivity;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class MyBalanceActivity extends PublicActivity implements HttpOnNextListener {
    private TextView tvBalance;
    private EditText etRechargeAmount;
    private TextView tvRecharge;

    private String amount = "";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MyBalanceActivity.class);
        context.startActivity(intent);
    }

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initMyBalanceRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.MY_BALANCE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_balance;
    }

    @Override
        protected void initView() {
        tvBalance = findViewById(R.id.tv_balance);

        etRechargeAmount = findViewById(R.id.et_recharge_amount);
        etRechargeAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //只能输入数字和小数点

        tvRecharge = findViewById(R.id.tv_recharge);
    }

    @Override
    protected void initData() {
        initMyBalanceRequest();
    }

    @Override
    protected void initEvent() {
        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = etRechargeAmount.getText().toString().trim();
                if (amount == null || amount.equals("")) {
                    ToastUtils.show("充值金额不能为空");
                    return;
                }
                PayModeActivity.startActivity(MyBalanceActivity.this, 3, amount);
            }
        });
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        LogUtils.i(resulte);
        if (mothead.equals(IndexApi.MY_BALANCE)) {
            MyBalanceBean data = new Gson().fromJson(resulte, new TypeToken<MyBalanceBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                tvBalance.setText(data.getData());
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {
        ToastUtils.show(e.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals(CommonUtils.BALANCE_REFRESH)) {
            initMyBalanceRequest();
        }
    }
}
