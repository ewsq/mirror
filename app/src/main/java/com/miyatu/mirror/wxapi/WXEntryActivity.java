package com.miyatu.mirror.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.miyatu.mirror.MyApp;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * create by: wangchao
 * 邮箱: 1064717519@qq.com
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WXEntryActivity", "onCreate");
        api = WXAPIFactory.createWXAPI(this, MyApp.WXAPPID);
        api.registerApp(MyApp.WXAPPID);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("WXEntryActivity", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        Log.e("WXEntryActivity", "onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXEntryActivity", "onResp");

        // TODO Auto-generated method stub
        // 获取code
        Log.e("WXEntryActivity", "errCode:" + resp.errCode);
        EventBus.getDefault().post(resp);
//        switch (resp.errCode) {
//            // 发送成功
//            case BaseResp.ErrCode.ERR_OK:
//                String code = ((SendAuth.Resp) resp).code;
//                getAccessToken(code);
//                Log.e("WXEntryActivity", "code:" + code);
//                String accessToken = PreferencesUtils.getString(this, PreferencesUtils.WEIXIN_ACCESS_TOKEN_KEY, "");
//                String openid = PreferencesUtils.getString(this, PreferencesUtils.WEIXIN_OPENID_KEY, "");
//                if (!"".equals(accessToken)) {
//                    // 有access_token，判断是否过期有效
//                    isExpireAccessToken(accessToken, openid);
//                } else {
//                    // 没有access_token
//                    getAccessToken(code);
//                }
//                break;
//        }
        finish();
    }


}
