package com.miyatu.mirror.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.AliPayBean;
import com.miyatu.mirror.bean.WxPayBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.CommonUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class PayModeActivity extends PublicActivity implements View.OnClickListener, HttpOnNextListener {

    private RelativeLayout rlBack;
    private TextView tvEdit;
    private LinearLayout linPayforAli;//支付宝支付
    private LinearLayout linPayforWx;//微信支付

    private int type = 0;           //支付类型 1：会员升级 2：量体数据
    private int itemID = 0;         //会员等级id或量体数据的id
    private double price = 0;       //价格

    private HttpManager manager;
    private IndexApi api;
    private Map<String,Object> params;

    public static void startActivity(Context context, int type, int itemID, double price){
        Intent intent = new Intent(context, PayModeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("itemID", itemID);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_mode;
    }

    private void initPayRequest(String method){
        params = new HashMap<>();
        params.put("token",getUserDataBean().getToken());
        params.put("type",type);
        params.put("item_id", itemID);
        manager = new HttpManager(this,this);
        api = new IndexApi(method);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_payfor_wechat:
                initPayRequest(IndexApi.WX_PAY);
                break;
            case R.id.iv_payfor_zhi:
                initPayRequest(IndexApi.ALI_PAY);
                break;
            case R.id.rl_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void initView(){
        rlBack = findViewById(R.id.rl_back);
        linPayforAli = findViewById(R.id.iv_payfor_zhi);
        linPayforAli.setOnClickListener(this);
        linPayforWx = findViewById(R.id.iv_payfor_wechat);
        linPayforWx.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 0);
        itemID = getIntent().getIntExtra("itemID", 0);
        price = getIntent().getDoubleExtra("price", 0);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if(mothead.equals(IndexApi.ALI_PAY)){
            AliPayBean data = new Gson().fromJson(resulte, new TypeToken<AliPayBean>(){}.getType());
            if(data.getStatus() == 1){
                aliPay(this,data.getResult());
            }
        }
        if(mothead.equals(IndexApi.WX_PAY)){
            WxPayBean data = new Gson().fromJson(resulte, new TypeToken<WxPayBean>(){}.getType());
            if(data.getStatus() == 1){
                wxPay(data.getResult());
            }
        }
    }

    @Override
    public void onError(ApiException e) {

    }

    //支付宝支付
    public void aliPay(Activity activity, String orderInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                Map<String, String> result = payTask.payV2(orderInfo, true);
                EventBus.getDefault().post(result);
            }
        }).start();
    }

    //微信支付
    public void wxPay(WxPayBean.ResultBean entity) {
        //"noncestr":"Xg2g1CEAQdHatK9R","package":"Sign=WXPay","partnerid":"1567830131","prepayid":"wx10192520802603eae3a0eea81333649600","timestamp":1575977120,"sign":"DCB29C60FBF7B78B4109EE9CD38CD7A1"}}
        PayReq request = new PayReq();
        request.appId = entity.getAppid();
        request.nonceStr = entity.getNoncestr();
        request.packageValue = entity.getPackageX();
        request.partnerId = entity.getPartnerid();
        request.prepayId = entity.getPrepayid();
        request.timeStamp = String.valueOf(entity.getTimestamp());
        request.sign = entity.getSign();
        MyApp.api.sendReq(request);
    }

    @Override
    public void onEvent(Object object) {
        if(object instanceof BaseResp){
            BaseResp resp = (BaseResp)object;
            if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

                if (resp.errCode == 0) {
                    ToastUtils.show("支付成功");
                    EventBus.getDefault().post(true);
                    EventBus.getDefault().post(CommonUtils.REFRESH);
                    finish();
                } else if (resp.errCode == -2) {
                    ToastUtils.show("取消支付");
                } else {
                    ToastUtils.show("参数错误");
                }
            }
        }else {
            Map<String, String> result = (Map<String, String>) object;
            //9000 订单支付成功
            //8000 正在处理中
            //4000 订单支付失败
            //6001 用户中途取消
            //6002 网络连接出错
            String status = result.get("resultStatus");
            if (status.equals("9000")) {
                ToastUtils.show("支付成功");
                EventBus.getDefault().post(true);
                EventBus.getDefault().post(CommonUtils.REFRESH);
                finish();
            }
            if (status.equals("8000")) {
                ToastUtils.show("正在处理中");
            }
            if (status.equals("4000")) {
                ToastUtils.show("订单支付失败");
            }
            if (status.equals("6001")) {
                ToastUtils.show("取消支付");
            }
            if (status.equals("6002")) {
                ToastUtils.show("网络连接出错");
            }
        }
    }
}
