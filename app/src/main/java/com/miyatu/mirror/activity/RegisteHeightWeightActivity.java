package com.miyatu.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.RegisterBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面 身高、体重
 */
public class RegisteHeightWeightActivity extends RxAppCompatActivity implements View.OnClickListener, HttpOnNextListener {
    private ImageView ivback=null;
    private EditText et_height=null;
    private EditText et_weight=null;
    private Button btn_nextstep_registe=null;

    private String mobile = "";
    private int code = 0;
    private String sessionId = "";
    private String password = "";
    private String nickName = "";
    private String email = "";
    private int sex = 0;
    private String birthday = "";

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_height_weight);
        initView();
    }

    private void initView() {
        ivback=findViewById(R.id.iv_registe_height_weight_back);
        ivback.setOnClickListener(this);
        et_height=findViewById(R.id.et_heightweight_height);
        et_height.setOnClickListener(this);
        et_weight=findViewById(R.id.et_heightweight_weight);
        et_weight.setOnClickListener(this);
        btn_nextstep_registe=findViewById(R.id.btn_registe_height_weight_registe);
        btn_nextstep_registe.setOnClickListener(this);

        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getIntExtra("code", 0);
        password = getIntent().getStringExtra("password");
        sessionId = getIntent().getStringExtra("session_id");
        nickName = getIntent().getStringExtra("nickname");
        email = getIntent().getStringExtra("email");
        sex = getIntent().getIntExtra("sex", 0);
        birthday = getIntent().getStringExtra("birthday");
    }

    private void initRequest(String height, String weight) {
        params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("session_id", sessionId);
        params.put("password", password);
        params.put("email", email);
        params.put("nickname", nickName);
        params.put("sex", sex);
        params.put("birthday", birthday);
        params.put("height", height);
        params.put("weight", weight);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.REGISTER);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_registe_height_weight_back:{
                finish();
                break;
            }
            case R.id.btn_registe_height_weight_registe:{
                String height = et_height.getText().toString().trim();
                String weight = et_weight.getText().toString().trim();
                if (height == null || height.equals("")) {
                    ToastUtils.show("请输入身高");
                    break;
                }
                if (weight == null || weight.equals("")) {
                    ToastUtils.show("请输入体重");
                    break;
                }
//                Intent intent=new Intent(RegisteHeightWeightActivity.this,HomeActivity.class);
//                startActivity(intent);
                initRequest(height, weight);
                break;
            }
        }
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.REGISTER)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            RegisterBean data = new Gson().fromJson(resulte, new TypeToken<RegisterBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功

                Intent intent = new Intent(RegisteHeightWeightActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

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
