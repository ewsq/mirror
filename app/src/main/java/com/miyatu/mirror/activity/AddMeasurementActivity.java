package com.miyatu.mirror.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.AddMeasurementAdapter;
import com.miyatu.mirror.bean.AddMeasurementBean;
import com.miyatu.mirror.bean.MeasurementBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.ui.PayModeActivity;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.LogUtils;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMeasurementActivity extends PublicActivity implements HttpOnNextListener {

    private Profile2ModelData profile2ModelData;
    private String taskId;
    private int apiType;
    private boolean hasEdit;

    private String userName;
    private String height;
    private int relativeID;

    private TextView toPay;

    private AddMeasurementBean.DataBean dataBean = new AddMeasurementBean.DataBean();


    private AddMeasurementAdapter adapter;
    private RecyclerView recyclerView;
    private List<MeasurementBean> measurementBeanList = new ArrayList<MeasurementBean>();

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest() {
        params = new HashMap<>();
        manager = new HttpManager(this, this);
        api = new IndexApi(IndexApi.ADD_MEASURE);
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", relativeID);
        params.put("measure", new Gson().toJson(measurementBeanList));
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_measurement;
    }

    @Override
    protected void initView() {
        toPay = findViewById(R.id.toPay);
        recyclerView = findViewById(R.id.recyclerView);
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

        initRecycleView();
        initRequest();
    }

    private void initRecycleView() {
        adapter = new AddMeasurementAdapter(R.layout.item_add_measurement, measurementBeanList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddMeasurementActivity.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onNext(String resulte, String mothead) {
        LogUtils.i(resulte);
        if (mothead.equals(IndexApi.ADD_MEASURE)) {
            AddMeasurementBean data = new Gson().fromJson(resulte, new TypeToken<AddMeasurementBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                dataBean = data.getData();

                toPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PayModeActivity.startActivity(AddMeasurementActivity.this, 2, dataBean.getMeasure_id(), relativeID, String.valueOf(dataBean.getPrice()));              //type为2表示是量体数据支付
                        finish();
                    }
                });
                EventBus.getDefault().post(CommonUtils.REFRESH);
                ToastUtils.show(data.getMsg());
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
