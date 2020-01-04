package com.miyatu.mirror.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.bean.AddMeasurementBean;
import com.miyatu.mirror.bean.MeasurementBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.ui.PayModeActivity;
import com.miyatu.mirror.util.LogUtils;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddMeasurementActivity extends PublicActivity implements HttpOnNextListener {
    private ProgressBar progressBar;
    private LinearLayout linZhiFuBao;
    private LinearLayout linWX;

    private Profile2ModelData profile2ModelData;
    private String taskId;
    private int apiType;
    private boolean hasEdit;

    private String userName;
    private String height;
    private int relativeID;

    private List<MeasurementBean> measurementBeanList = new ArrayList<MeasurementBean>();

    private AddMeasurementBean.DataBean dataBean;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;
    private Map<String, RequestBody> bodyMap;
    private List<MultipartBody.Part> parts;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_measurement;
    }

    @Override
    protected void initView() {
        progressBar = findViewById(R.id.progress_bar);
        linZhiFuBao = findViewById(R.id.linZhiFuBao);
        linWX = findViewById(R.id.linWX);
    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString("userName");
            height = bundle.getString("height");
            relativeID = bundle.getInt("relativeID");
            measurementBeanList = bundle.getParcelableArrayList("measurementBeanList");
        }
        System.out.println("####step meaSize=" + measurementBeanList.size());

        params = new HashMap<>();
        manager = new HttpManager(this, this);
        api = new IndexApi(IndexApi.ADD_MEASURE);
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", relativeID);
        params.put("measure", new Gson().toJson(measurementBeanList));
        Log.i("wangchao", "initData: "+ new Gson().toJson(measurementBeanList));
        api.setParams(params);
        manager.doHttpDeal(api);

//        initRequest();
//        initAddMeasure();
    }

    @Override
    protected void initEvent() {
        linZhiFuBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linWX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void updateView() {

    }

//    private void initAddMeasure() {
//        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), getUserDataBean().getToken());
//        RequestBody relativeIDBody = RequestBody.create(MediaType.parse("text/plain"), relativeID + "");
//        RequestBody measureBody = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(measurementBeanList));
//
//        bodyMap.put("token", tokenBody);
//        bodyMap.put("relative_id", relativeIDBody);
//        bodyMap.put("measure", measureBody);
//
//        api.setBodyMap(bodyMap);
//        api.setParts(parts);
//        manager.doHttpDeal(api);
//    }

    @Override
    public void onNext(String resulte, String mothead) {
        LogUtils.i(resulte);
        if (mothead.equals(IndexApi.ADD_MEASURE)) {
            AddMeasurementBean data = new Gson().fromJson(resulte, new TypeToken<AddMeasurementBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                dataBean = data.getData();
//                progressBar.setVisibility(View.GONE);
//                linZhiFuBao.setVisibility(View.VISIBLE);
//                linWX.setVisibility(View.VISIBLE);
                PayModeActivity.startActivity(this, 2, dataBean.getMeasure_id(), dataBean.getPrice());
                finish();
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
