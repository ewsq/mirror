package com.miyatu.mirror.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.bean.BaseBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

/*
问题与反馈页面
* */
public class FeedbackActivity extends PublicActivity implements HttpOnNextListener {

    private RelativeLayout rlBack;
    private TextView tvEdit;
    private EditText etPhone;
    private EditText etFeedbackTitle;
    private EditText etFeedbackContent;
    private TextView tvCommit;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest(String mobile, String title, String content) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("mobile", mobile);
        params.put("title", title);
        params.put("content", content);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.ADD_QUESTION);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        etFeedbackContent = findViewById(R.id.et_feedback_content);
        etFeedbackTitle = findViewById(R.id.et_feedback_title);
        etPhone = findViewById(R.id.et_phone);
        tvCommit = findViewById(R.id.tv_commit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = etPhone.getText().toString().trim();
                String title = etFeedbackTitle.getText().toString().trim();
                String content = etFeedbackContent.getText().toString().trim();
                if (mobile == null || mobile.equals("")) {
                    ToastUtils.show("请输入手机号");
                    return;
                }
                if (title == null || title.equals("")) {
                    ToastUtils.show("请输入标题");
                    return;
                }
                if (content == null || content.equals("")) {
                    ToastUtils.show("请输入内容");
                    return;
                }
                initRequest(mobile, title, content);
            }
        });
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.ADD_QUESTION)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            BaseBean data = new Gson().fromJson(resulte, new TypeToken<BaseBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                finish();
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
