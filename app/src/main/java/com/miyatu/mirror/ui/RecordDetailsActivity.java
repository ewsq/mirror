package com.miyatu.mirror.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.RecordDetailsAdapter;
import com.miyatu.mirror.bean.RecordDetailsBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

/*
记录详情页
* */
public class RecordDetailsActivity extends PublicActivity implements HttpOnNextListener {

    private RelativeLayout rlBack;
    private ImageView ivSex;
    private TextView tvName;
    private TextView tvHeight;
    private TextView tvWeight;

    private RecyclerView recyclerView;
    private RecordDetailsAdapter adapter;

    private int relativeID;
    private int measureID;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest(int relativeID, int measureID) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", relativeID);
        params.put("measure_id", measureID);
        manager = new HttpManager(this,(RxAppCompatActivity)RecordDetailsActivity.this);
        api = new IndexApi(IndexApi.MEASURE_INFO);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context, int relativeID, int measureID){
        Intent intent = new Intent(context, RecordDetailsActivity.class);
        intent.putExtra("relativeID", relativeID);
        intent.putExtra("measureID", measureID);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_details;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        ivSex = findViewById(R.id.iv_sex);
        tvName = findViewById(R.id.tv_name);
        tvHeight = findViewById(R.id.tv_height);
        tvWeight = findViewById(R.id.tv_weight);
        recyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    @Override
    protected void initData() {
        relativeID = getIntent().getIntExtra("relativeID", 0);
        measureID = getIntent().getIntExtra("measureID", 0);
        initRequest(relativeID, measureID);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//返回键
            }
        });
    }

    @Override
    protected void updateView() {

    }

    private void initRecyclerView() {
        adapter = new RecordDetailsAdapter(R.layout.item_record_details, null);
        recyclerView.setLayoutManager(new GridLayoutManager(RecordDetailsActivity.this, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.MEASURE_INFO)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            RecordDetailsBean data = new Gson().fromJson(resulte, new TypeToken<RecordDetailsBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                Glide.with(this).load(data.getData().getGender() == 1? R.mipmap.icon_men : R.mipmap.icon_women).placeholder( R.mipmap.edit_person_info_headicon).into(ivSex);
                tvName.setText(data.getData().getRelative());
                tvHeight.setText(data.getData().getHeight() + "cm");
                tvWeight.setText(data.getData().getWeight() + "kg");
                adapter.setNewData(data.getData().getMeasure());
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {
        ToastUtils.show(e.getMessage());
    }
}
