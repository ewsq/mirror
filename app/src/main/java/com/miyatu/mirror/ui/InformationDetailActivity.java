package com.miyatu.mirror.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.InformationDetailBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/*
  消息详情页面
* */
public class InformationDetailActivity extends PublicActivity implements HttpOnNextListener {

    private RelativeLayout rlBack;
    private TextView tvEdit;
    private TextView tvTitle;
    private TextView tvDate;
    private ImageView ivImg;
    private WebView webView;

    private int id = 0;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest(int id) {
        params = new HashMap<>();
        params.put("id", id);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.MESSAGE_INFO);
        api.setParams(params);
        manager.doHttpDeal(api);
    }


    public static void startActivity(Context context, int id){
        Intent intent = new Intent(context,InformationDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_information_detail;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        rlBack = findViewById(R.id.rl_back);
        tvTitle  = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date);
        webView = findViewById(R.id.webView);
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", 0);
        initRequest(id);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.MESSAGE_INFO)) {
            LogUtils.i(resulte);
            InformationDetailBean data = new Gson().fromJson(resulte, new TypeToken<InformationDetailBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                tvTitle.setText(data.getData().getTitle());
                tvDate.setText(data.getData().getDate());

                String[] str = data.getData().getContent().split("<img src=\"");
                String imgUrl = str[0];
                for (int i = 1; i < str.length; i ++) {
                    str[i] = "http://mojing.meyatu.com" + str[i];
                    imgUrl = imgUrl + "<img style=\"max-width:100%;height:auto\" src=\"" + str[i];
                }
                webView.getSettings().setDefaultTextEncodingName("UTF -8");
                webView.getSettings().setTextZoom(75);        //设置文字大小百分比
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                webView.setLayoutParams(layoutParams);
                webView.loadData(imgUrl, "text/html; charset=UTF-8", null);

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
