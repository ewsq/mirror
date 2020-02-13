package com.miyatu.mirror.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.EditFriendVolumeRecordFragmentAdapter;
import com.miyatu.mirror.bean.BaseBean;
import com.miyatu.mirror.bean.FriendVolumeRecordFragmentBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.ui.PayModeActivity;
import com.miyatu.mirror.ui.RecordDetailsActivity;
import com.miyatu.mirror.util.CommonUtils;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditFriendVolumeRecordFragment extends PublicFragment implements HttpOnNextListener {
    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private EditFriendVolumeRecordFragmentAdapter adapter;

    private ImageView ivSelectAll;
    private TextView tvDelete;

    private boolean isSelectAll = false;

    private String deleteIDS = "";

    private void initRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("page", page);
        params.put("pageSize", pageSize);
        manager = new HttpManager(this,(RxAppCompatActivity)getActivity());
        api = new IndexApi(IndexApi.INDEX);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initDelRequest(String ids) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("measure_id", ids);
        manager = new HttpManager(this,(RxAppCompatActivity)getActivity());
        api = new IndexApi(IndexApi.DEL_MEASURE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_edit_friend_volume_record;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        ivSelectAll = view.findViewById(R.id.iv_select_all);
        tvDelete = view.findViewById(R.id.tv_delete);

        initRecyclerView();
    }

    @Override
    protected void initData() {
        initRequest();

        initRefreshLayout();
    }

    @Override
    protected void initEvent() {
        ivSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FriendVolumeRecordFragmentBean.DataBean> dataBeanList = (List<FriendVolumeRecordFragmentBean.DataBean>) adapter.getData();
                if (isSelectAll == true) {
                    isSelectAll = false;
                    ivSelectAll.setImageResource(R.mipmap.unselected);
                    for (int i = 0; i < dataBeanList.size(); i ++) {
                        dataBeanList.get(i).setSelect(false);
                    }
                }
                else {
                    isSelectAll = true;
                    ivSelectAll.setImageResource(R.mipmap.selected);
                    for (int i = 0; i < dataBeanList.size(); i ++) {
                        dataBeanList.get(i).setSelect(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FriendVolumeRecordFragmentBean.DataBean> dataBeanList = (List<FriendVolumeRecordFragmentBean.DataBean>) adapter.getData();
                for (int i = 0; i < dataBeanList.size(); i ++) {
                    if (dataBeanList.get(i).isSelect() == true) {
                        if (i == 0) {
                            deleteIDS = String.valueOf(dataBeanList.get(i).getId());
                        }
                        else {
                            deleteIDS = deleteIDS + "," + String.valueOf(dataBeanList.get(i).getId());
                        }
                    }
                }
                initDelRequest(deleteIDS);
            }
        });
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
            isSelectAll = false;
            ivSelectAll.setImageResource(R.mipmap.unselected);
            initRequest();
        });
    }

    private void initRecyclerView() {
        adapter = new EditFriendVolumeRecordFragmentAdapter(R.layout.item_edit_friend_volume_record, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            isLoadMore = true;
            initRequest();
        }, recyclerView);

        adapter.setOnItemChildClickListener(new EditFriendVolumeRecordFragmentAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FriendVolumeRecordFragmentBean.DataBean dataBean = (FriendVolumeRecordFragmentBean.DataBean)adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_pay:
                        PayModeActivity.startActivity(getActivity(), 2, dataBean.getId(), dataBean.getRelative_id(), dataBean.getPrice());
                        break;
                    case R.id.tv_more:
                        RecordDetailsActivity.startActivity(getActivity(), dataBean.getRelative_id(), dataBean.getId());
                        break;
                    case R.id.iv_select:
                        if (dataBean.isSelect() == true) {
                            dataBean.setSelect(false);
                            isSelectAll = false;
                            ivSelectAll.setImageResource(R.mipmap.unselected);
                        }
                        else {
                            dataBean.setSelect(true);
                            int num = 0;
                            List<FriendVolumeRecordFragmentBean.DataBean> dataBeanList = (List<FriendVolumeRecordFragmentBean.DataBean>) adapter.getData();
                            for (int i = 0; i < dataBeanList.size(); i ++) {
                                if (dataBeanList.get(i).isSelect() == false) {
                                    num ++;
                                }
                            }
                            if (num > 0) {
                                isSelectAll = false;
                                ivSelectAll.setImageResource(R.mipmap.unselected);
                            }
                            else {
                                isSelectAll = true;
                                ivSelectAll.setImageResource(R.mipmap.selected);
                            }
                        }
                        adapter.notifyItemChanged(position);
                        break;
                }
            }
        });
    }

    private void initContent(List<FriendVolumeRecordFragmentBean.DataBean> dataBeanList) {
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
            FriendVolumeRecordFragmentBean data = new Gson().fromJson(resulte, new TypeToken<FriendVolumeRecordFragmentBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                initContent(data.getData());
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.DEL_MEASURE)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            BaseBean data = new Gson().fromJson(resulte, new TypeToken<BaseBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                ToastUtils.show("删除成功");
                isSelectAll = false;
                ivSelectAll.setImageResource(R.mipmap.unselected);
                page = 1;
                EventBus.getDefault().post(CommonUtils.REFRESH);
                initRequest();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals(CommonUtils.REFRESH)) {
            page = 1;
            initRequest();
        }
    }
}
