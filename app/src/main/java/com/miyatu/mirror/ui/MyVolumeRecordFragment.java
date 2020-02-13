package com.miyatu.mirror.ui;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.MyVolumeRecordFragmentAdapter;
import com.miyatu.mirror.bean.MyVolumeRecordFragmentBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yhd on 2019/11/19.
 * Email 1443160259@qq.com
 */

public class MyVolumeRecordFragment extends PublicFragment implements HttpOnNextListener {

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private MyVolumeRecordFragmentAdapter adapter;

    private void initRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", getUserDataBean().getRelative().getRelative_id());
        params.put("page", page);
        params.put("pageSize", pageSize);
        manager = new HttpManager(this,(RxAppCompatActivity)getActivity());
        api = new IndexApi(IndexApi.INDEX);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_myvolumerecord;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        initRecyclerView();

        initclick();//点击设置
        initEdit();//编辑模式
    }

    @Override
    protected void initData() {
        initRequest();

        initRefreshLayout();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

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

    private void initEdit() {

    }
    private void initclick() {

    }

    private void initRecyclerView() {
        adapter = new MyVolumeRecordFragmentAdapter(R.layout.item_my_volume_record_fragment, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            isLoadMore = true;
            initRequest();
        }, recyclerView);

        adapter.setOnItemChildClickListener(new MyVolumeRecordFragmentAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyVolumeRecordFragmentBean.DataBean dataBean = (MyVolumeRecordFragmentBean.DataBean)adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_pay:
                        PayModeActivity.startActivity(getActivity(), 2, dataBean.getId(), dataBean.getRelative_id(), dataBean.getPrice());
                        break;
                    case R.id.tv_more:
                        RecordDetailsActivity.startActivity(getActivity(), dataBean.getRelative_id(), dataBean.getId());
                        break;
                }
            }
        });
    }

    private void initContent(List<MyVolumeRecordFragmentBean.DataBean> dataBeanList) {
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
        if (mothead.equals(IndexApi.INDEX)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            MyVolumeRecordFragmentBean data = new Gson().fromJson(resulte, new TypeToken<MyVolumeRecordFragmentBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                initContent(data.getData());
                return;
            }
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.i(e.getMessage());
        ToastUtils.show(e.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals(CommonUtils.REFRESH)) {
            page = 1;
            initRequest();
        }
    }
}
