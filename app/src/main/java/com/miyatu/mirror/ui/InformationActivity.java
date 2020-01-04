package com.miyatu.mirror.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.adapter.InformationAdapter;
import com.miyatu.mirror.bean.InformationDatabean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends PublicActivity implements HttpOnNextListener {

    private RelativeLayout rlBack;
    private TextView tvEdit;
    private RecyclerView recyclerView;

    private InformationAdapter informationAdapter;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest() {
        params = new HashMap<>();
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.MESSAGE_LIST);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context,InformationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_information;
    }

    protected void  initView(){
        rlBack = findViewById(R.id.rl_back);
        recyclerView = findViewById(R.id.recyclerView);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void initData() {
        initRecyclerView();
        initRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initRecyclerView() {
        informationAdapter = new InformationAdapter(R.layout.item_information, null);
        recyclerView.setAdapter(informationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(InformationActivity.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(informationAdapter);

        informationAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            InformationDatabean.DataBean dataBean = (InformationDatabean.DataBean)informationAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.iv_img:
                    InformationDetailActivity.startActivity(this, dataBean.getId());
                    break;
            }
        });
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.MESSAGE_LIST)) {
            LogUtils.i(resulte);
            InformationDatabean data = new Gson().fromJson(resulte, new TypeToken<InformationDatabean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                informationAdapter.setNewData(data.getData());

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

