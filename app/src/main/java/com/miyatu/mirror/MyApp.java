package com.miyatu.mirror;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;

public class MyApp extends Application {
    public static final String baseUrl = "http://mojing.meyatu.com/index/";
    public static final String imageUrl = "http://mojing.meyatu.com";

    public static final String WXAPPID = "wx5a8bced094340e09";
    public static final String WXSECRET = "af0e3e42273d82b0c3c2e181a5b72a6a";

    public static final int PROFILE2MEASURE = 0;
    public static final int IMAGE2MEASURE = 1;

    public static final int FACING_BACK = 0;
    public static final int FACING_FRONT = 1;

    // IWXAPI 是第三方app和微信通信的openApi接口
    public static IWXAPI api;

    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        RxRetrofitApp.init(this,baseUrl);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WXAPPID, true);
    }

}