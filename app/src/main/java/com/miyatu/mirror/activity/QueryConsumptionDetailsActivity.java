package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.QueryConsumptionDetailsAdapter;
import com.miyatu.mirror.bean.QueryConsumptionDetailsBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryConsumptionDetailsActivity extends PublicActivity implements HttpOnNextListener {

    private RelativeLayout rlBack;

    private RecyclerView recyclerView;
    private QueryConsumptionDetailsAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("page", page);
        params.put("pageSize", pageSize);
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.QUERY_CONSUMPTION_DETAILS);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, QueryConsumptionDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_query_consumption_details;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);

        initRecyclerView();
    }

    @Override
    protected void initData() {
        initRequest();

        initRefreshLayout();
    }

    //初始化刷新控件
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeColors(Color.parseColor("#f39800"));
        refreshLayout.setOnRefreshListener(() -> {
            isRefreshing = true;
            page = 1;
            initRequest();
        });
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void updateView() {

    }

    private void initRecyclerView() {
        adapter = new QueryConsumptionDetailsAdapter(R.layout.item_query_consumption_details, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(QueryConsumptionDetailsActivity.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            isLoadMore = true;
            initRequest();
        }, recyclerView);
    }

    private void initContent(List<QueryConsumptionDetailsBean.DataBean> dataBeanList) {
        if (isLoadMore) {
            isLoadMore = false;
            adapter.loadMoreComplete();
            if (dataBeanList.size() < pageSize) {
                adapter.loadMoreEnd();
            }
            adapter.addData(dataBeanList);
        } else {
            refreshLayout.setRefreshing(false);
            isRefreshing = false;
            adapter.setNewData(dataBeanList);
        }
        page ++;
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.QUERY_CONSUMPTION_DETAILS)) {
            QueryConsumptionDetailsBean data = new Gson().fromJson(resulte, new TypeToken<QueryConsumptionDetailsBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                initContent(data.getData());
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {

    }
}
