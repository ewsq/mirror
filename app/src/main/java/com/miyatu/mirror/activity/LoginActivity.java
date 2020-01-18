package com.miyatu.mirror.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.WebViewActivity;
import com.miyatu.mirror.bean.BaseBean;
import com.miyatu.mirror.bean.ProtocolBean;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.bean.WXAccessTokenInfo;
import com.miyatu.mirror.bean.WXUserInfo;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.HttpUtils;
import com.miyatu.mirror.util.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.miyatu.mirror.MyApp.api;


/**
 * 登陆页面
 */
public class LoginActivity extends PublicActivity implements View.OnClickListener, HttpOnNextListener {
    private ImageView iv_cancel = null, iv_loginbywx = null;
    private EditText et_account = null, et_password = null;
    private Button btn_login = null;
    private TextView tv_registe = null, tv_forgetpasswd = null, agreement = null;

    private HttpManager manager;
    private IndexApi indexApi;
    private Map<String, Object> params;

    private String userOpenID = "";

    private com.miyatu.mirror.util.HttpUtils httpUtils;

    private ProtocolBean.DataBean protocolBean;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void initLoginRequest() {
        params = new HashMap<>();
        manager = new HttpManager(this,(RxAppCompatActivity)this);
    }

    private void initProtocolRequest() {
        params = new HashMap<>();
        params.put("type", "register");
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        indexApi = new IndexApi(IndexApi.GET_PROTOCOL);
        indexApi.setParams(params);
        manager.doHttpDeal(indexApi);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    protected void initView() {
        iv_cancel = findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);
        iv_loginbywx = findViewById(R.id.iv_loginbywx);
        iv_loginbywx.setOnClickListener(this);
        et_account = findViewById(R.id.et_account);
        et_account.setOnClickListener(this);
        et_password = findViewById(R.id.et_password);
        et_password.setOnClickListener(this);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_registe = findViewById(R.id.tv_registe);
        tv_registe.setOnClickListener(this);
        tv_forgetpasswd = findViewById(R.id.tv_forgetpasswd);
        tv_forgetpasswd.setOnClickListener(this);
        agreement = findViewById(R.id.agreement);
        agreement.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        httpUtils = new com.miyatu.mirror.util.HttpUtils();
        initLoginRequest();
        initProtocolRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel: {
                finish();
                break;
            }
            case R.id.btn_login: {
                String account = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (account == null || account.equals("")) {
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                if (password == null || password.equals("")) {
                    ToastUtils.show("密码不能为空");
                    return;
                }
                params.put("mobile", account);
                params.put("password", password);
                params.put("type", 0);          //0表示账号密码登录
                indexApi = new IndexApi(IndexApi.LOGIN);
                indexApi.setParams(params);
                manager.doHttpDeal(indexApi);

                break;
            }
            case R.id.tv_registe: {
                toActivity(RegisteActivity.class);
                break;
            }
            case R.id.tv_forgetpasswd: {
                ForgetPasswordActivity.startActivity(LoginActivity.this);
                break;
            }
            case R.id.iv_loginbywx: {
                loginWX();
                break;
            }
            case R.id.agreement: {
                WebViewActivity.startActivity(LoginActivity.this, protocolBean.getContent());
                break;
            }
        }
    }


    public void loginWX() {
        regToWx();
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            ToastUtils.show("您还未安装微信客户端！");
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }
    private void regToWx() {
        // 将应用的appId注册到微信
        api.registerApp(MyApp.WXAPPID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(MyApp.WXAPPID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object object) {
        if (object instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) object;
            switch (resp.errCode) {
                // 发送成功
                case BaseResp.ErrCode.ERR_OK:
                    String code = resp.code;
//                getAccessToken(code);
                    Log.e("WXEntryActivity", "code:" + code);
                    String accessToken = PreferencesUtils.getString(this, PreferencesUtils.WEIXIN_ACCESS_TOKEN_KEY, "");
                    String openid = PreferencesUtils.getString(this, PreferencesUtils.WEIXIN_OPENID_KEY, "");
                    if (!"".equals(accessToken)) {
                        // 有access_token，判断是否过期有效
                        isExpireAccessToken(accessToken, openid);
                    } else {
                        // 没有access_token
                        getAccessToken(code);
                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    ToastUtils.show("拒绝授权");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtils.show("取消授权");
                    break;
            }
        }
    }
    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + MyApp.WXAPPID +
                "&secret=" + MyApp.WXSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        httpUtils.httpRequest(url, new HttpUtils.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.i(response);
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }
            @Override
            public void onError(int errorCode, final String errorMsg) {
                LogUtils.i(errorMsg);
                ToastUtils.show("错误信息: " + errorMsg);
            }
            @Override
            public void onFailure(IOException e) {
                LogUtils.i(e.getMessage());
                ToastUtils.show("登录失败");
            }
        });
    }
    /**
     * 判断accesstoken是过期
     *
     * @param accessToken token
     * @param openid      授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        httpUtils.httpRequest(url, new HttpUtils.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.i(response);
                if (validateSuccess(response)) {
                    // accessToken没有过期，获取用户信息
                    getUserInfo(accessToken, openid);
                } else {
                    // 过期了，使用refresh_token来刷新accesstoken
                    refreshAccessToken();
                }
            }
            @Override
            public void onError(int errorCode, final String errorMsg) {
                LogUtils.i(errorMsg);
                ToastUtils.show("错误信息: " + errorMsg);
            }
            @Override
            public void onFailure(IOException e) {
                LogUtils.i(e.getMessage());
                ToastUtils.show("登录失败");
            }
        });
    }
    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }
    /**
     * 获取用户信息
     **/
    private void getUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;
        httpUtils.httpRequest(url, new HttpUtils.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.i("用户信息获取结果：" + response);
                // 解析获取的用户信息
                WXUserInfo userInfo = new Gson().fromJson(response, WXUserInfo.class);
                userOpenID = userInfo.getOpenid();
                params = new HashMap<>();
                params.put("type", 1);          //1表示微信登录
                params.put("open_id", userOpenID);
                indexApi = new IndexApi(IndexApi.LOGIN);
                indexApi.setParams(params);
                manager.doHttpDeal(indexApi);
            }
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtils.show("错误信息: " + errorMsg);
            }
            @Override
            public void onFailure(IOException e) {
                ToastUtils.show("获取用户信息失败");
            }
        });
    }
    /**
     * 刷新获取新的access_token
     **/
    private void refreshAccessToken() {
        // 从本地获取以存储的refresh_token
        final String refreshToken = PreferencesUtils.getString(this, PreferencesUtils.WEIXIN_REFRESH_TOKEN_KEY, "");
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + MyApp.WXAPPID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;
        // 请求执行
        httpUtils.httpRequest(url, new HttpUtils.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.i("refreshAccessToken: " + response);
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }
            @Override
            public void onError(int errorCode, final String errorMsg) {
                LogUtils.i(errorMsg);
                ToastUtils.show("错误信息: " + errorMsg);
                // 重新请求授权
//                loginWeixin(WXEntryActivity.this.getApplicationContext(), GeneralAppliction.sApi);
            }
            @Override
            public void onFailure(IOException e) {
                LogUtils.i(e.getMessage());
                ToastUtils.show("登录失败");
                // 重新请求授权
//                loginWeixin(WXEntryActivity.this.getApplicationContext(), GeneralAppliction.sApi);
            }
        });
    }
    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            WXAccessTokenInfo tokenInfo = new Gson().fromJson(response, WXAccessTokenInfo.class);
            LogUtils.i(tokenInfo.toString());
            // 保存信息到手机本地
            saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            ToastUtils.show("授权口令获取失败");
//            // 授权口令获取失败，解析返回错误信息
//            WXErrorInfo wxErrorInfo = mGson.fromJson(response, WXErrorInfo.class);
//            Logger.e(wxErrorInfo.toString());
//            // 提示错误信息
//            showMessage("错误信息: " + wxErrorInfo.getErrmsg());
        }
    }
    private void saveAccessInfotoLocation(WXAccessTokenInfo tokenInfo) {
        PreferencesUtils.putString(this, PreferencesUtils.WEIXIN_REFRESH_TOKEN_KEY, tokenInfo.getRefresh_token());
        PreferencesUtils.putString(this, PreferencesUtils.WEIXIN_ACCESS_TOKEN_KEY, tokenInfo.getAccess_token());
        PreferencesUtils.putString(this, PreferencesUtils.WEIXIN_OPENID_KEY, tokenInfo.getOpenid());
    }



    private void toActivity(Class targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.LOGIN)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            BaseBean baseBean = new Gson().fromJson(resulte, new TypeToken<BaseBean>(){}.getType());
            if (baseBean.getStatus() == 1) {
                UserDatabean data = new Gson().fromJson(resulte, new TypeToken<UserDatabean>(){}.getType());
                PreferencesUtils.putString(LoginActivity.this, PreferencesUtils.USERDATA, new Gson().toJson(data.getData()));
                EventBus.getDefault().post(data.getData());
                EventBus.getDefault().post(CommonUtils.REFRESH);
                finish();
                return;
            }
            else {
                BindPhoneActivity.startActivity(LoginActivity.this, userOpenID);
                finish();
            }
        }
        if (mothead.equals(IndexApi.GET_PROTOCOL)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            ProtocolBean data = new Gson().fromJson(resulte, new TypeToken<ProtocolBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                protocolBean = data.getData();
                agreement.setText("《" + data.getData().getTitle() + "》");
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.i(e.getMessage());
        ToastUtils.show(e.getMessage());
    }
}
