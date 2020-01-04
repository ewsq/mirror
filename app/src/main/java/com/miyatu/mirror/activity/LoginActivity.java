package com.miyatu.mirror.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.WebViewActivity;
import com.miyatu.mirror.bean.ProtocolBean;
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


/**
 * 登陆页面
 */
public class LoginActivity extends PublicActivity implements View.OnClickListener, HttpOnNextListener {
    private ImageView iv_cancel = null, iv_loginbywx = null, iv_loginbyzfb = null, iv_loginbyqq = null;
    private EditText et_account = null, et_password = null;
    private Button btn_login = null;
    private TextView tv_registe = null, tv_forgetpasswd = null, agreement = null;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private ProtocolBean.DataBean protocolBean;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void initLoginRequest(String mobile, String password) {
        params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.LOGIN);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initProtocolRequest() {
        params = new HashMap<>();
        params.put("type", "register");
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.GET_PROTOCOL);
        api.setParams(params);
        manager.doHttpDeal(api);
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
        iv_loginbyzfb = findViewById(R.id.iv_loginbyzfb);
        iv_loginbyzfb.setOnClickListener(this);
        iv_loginbyqq = findViewById(R.id.iv_loginbyqq);
        iv_loginbyqq.setOnClickListener(this);
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
//            case R.id.et_account: {
//                Toast.makeText(LoginActivity.this, "请输入账号:"
//                        , Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case R.id.et_password: {
//                Toast.makeText(LoginActivity.this, "请输入密码:"
//                                + et_account.getText()
//                        , Toast.LENGTH_SHORT).show();
//                break;
//            }
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
                initLoginRequest(account, password);
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
                Toast.makeText(LoginActivity.this, "微信登陆"
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.iv_loginbyzfb: {
                Toast.makeText(LoginActivity.this, "支付宝登陆"
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.iv_loginbyqq: {
                Toast.makeText(LoginActivity.this, "QQ登陆"
                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.agreement: {
                WebViewActivity.startActivity(LoginActivity.this, protocolBean.getContent());
                break;
            }

        }
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
            UserDatabean data = new Gson().fromJson(resulte, new TypeToken<UserDatabean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                PreferencesUtils.putString(LoginActivity.this, PreferencesUtils.USERDATA, new Gson().toJson(data.getData()));

                EventBus.getDefault().post(data.getData());
                EventBus.getDefault().post(CommonUtils.REFRESH);
                finish();
                return;
            }
            ToastUtils.show(data.getMsg());
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
    }
}
