package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.adapter.AccountBindAdapter;
import com.miyatu.mirror.adapter.AccountManageAdapter;
import com.miyatu.mirror.bean.AccountBindBean;
import com.miyatu.mirror.bean.BaseBean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManage extends PublicActivity implements View.OnClickListener, HttpOnNextListener {
    private ImageView iv_manage_back;//返回

    private ImageView iv_bind_sex;//性別
    private ImageView iv_bind_edit;//编辑
    private TextView tv_bind_name;//姓名
    private TextView tv_bind_height;//身高

    private ImageView ivSelectAll;               //全选图标
    private RelativeLayout relSelectAll;         //全选栏
    private boolean isSelectAll = false;        //是否全选
    private TextView tv_delete;                 //删除

    private RecyclerView recyclerView;
    private AccountManageAdapter adapter;
    private List<AccountBindBean.DataBean> dataBeanList = new ArrayList<>();

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initGetRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        manager = new HttpManager(this,(RxAppCompatActivity)AccountManage.this);
        api = new IndexApi(IndexApi.RELATIVE_LIST);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initDelRequest(String relativeID) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", relativeID);
        manager = new HttpManager(this,(RxAppCompatActivity)AccountManage.this);
        api = new IndexApi(IndexApi.DEL_RELATIVE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AccountManage.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_manage;
    }

    protected void initView() {
        recyclerView=findViewById(R.id.recyclerView);

        iv_manage_back = findViewById(R.id.iv_manage_back);
        iv_manage_back.setOnClickListener(this);

        ivSelectAll = findViewById(R.id.iv_select_all);
        relSelectAll = findViewById(R.id.rel_select_all);
        relSelectAll.setOnClickListener(this);

        tv_delete = findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);

        iv_bind_sex=findViewById(R.id.iv_bind_sex);

        iv_bind_edit=findViewById(R.id.iv_bind_edit);
        iv_bind_edit.setOnClickListener(this);

        tv_bind_name=findViewById(R.id.tv_bind_name);
        tv_bind_height=findViewById(R.id.tv_bind_height);

        initRecyclerView();
    }
    protected void initData() {
        initMyAccount();
        initGetRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initRecyclerView() {
        adapter = new AccountManageAdapter(R.layout.lv_account_manage_item, dataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AccountManage.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new AccountBindAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AccountBindBean.DataBean dataBean = (AccountBindBean.DataBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_edit:
                        AddAccountBind.startActivity(AccountManage.this, dataBean, AddAccountBind.IS_OTHER_ACCOUNT);
                        break;
                    case R.id.iv_select:
                        if (dataBeanList.get(position).isSelect() == true) {
                            dataBeanList.get(position).setSelect(false);

                            isSelectAll = false;
                            ivSelectAll.setImageResource(R.mipmap.unselected);
                        }
                        else {
                            int flag = 0;
                            dataBeanList.get(position).setSelect(true);
                            for (int i = 0; i < dataBeanList.size(); i ++) {
                                if (dataBeanList.get(i).isSelect() == false) {
                                    flag ++;
                                }
                            }
                            if (flag > 0) {
                                isSelectAll = false;
                                ivSelectAll.setImageResource(R.mipmap.unselected);
                            }
                            else {
                                isSelectAll = true;
                                ivSelectAll.setImageResource(R.mipmap.selected);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void initMyAccount() {
        if (getUserDataBean().getRelative().getGender() == 0) {
            iv_bind_sex.setVisibility(View.GONE);
        }
        else if (getUserDataBean().getRelative().getGender() == 1) {
            iv_bind_sex.setImageResource(R.mipmap.man_select);
        }
        else {
            iv_bind_sex.setImageResource(R.mipmap.woman_select);
        }
        tv_bind_name.setText(getUserDataBean().getRelative().getRelative());
        tv_bind_height.setText(getUserDataBean().getRelative().getHeight() + "cm");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_manage_back: {
                finish();
                break;
            }
            case R.id.iv_bind_edit: {
                AccountBindBean.DataBean dataBean = new AccountBindBean.DataBean();
                dataBean.setRelative(getUserDataBean().getRelative().getRelative());
                dataBean.setId(getUserDataBean().getRelative().getRelative_id());
                dataBean.setGender(getUserDataBean().getRelative().getGender());
                dataBean.setHeight(getUserDataBean().getRelative().getHeight());
                dataBean.setWeight(getUserDataBean().getRelative().getWeight());
                AddAccountBind.startActivity(AccountManage.this, dataBean, AddAccountBind.IS_MY_ACCOUNT);
                break;
            }
            case R.id.tv_delete: {
                initDelRequest(initDelIDS());
                break;
            }
            case R.id.rel_select_all:
                if (isSelectAll == true) {
                    isSelectAll = false;
                    ivSelectAll.setImageResource(R.mipmap.unselected);
                }
                else {
                    isSelectAll = true;
                    ivSelectAll.setImageResource(R.mipmap.selected);
                }
                for (int i = 0; i < dataBeanList.size(); i ++) {
                    if (isSelectAll == true) {
                        dataBeanList.get(i).setSelect(true);
                    }
                    else {
                        dataBeanList.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.RELATIVE_LIST)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            AccountBindBean data = new Gson().fromJson(resulte, new TypeToken<AccountBindBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                dataBeanList = data.getData();
                adapter.setNewData(dataBeanList);
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.DEL_RELATIVE)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            BaseBean data = new Gson().fromJson(resulte, new TypeToken<BaseBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                initGetRequest();
                EventBus.getDefault().post(data);
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object o) {
        initMyAccount();

        recyclerView.removeAllViews();          //防止recyclerView绑定新的数据时错乱
        initGetRequest();
    }

    private String initDelIDS() {
        String delIDS = "";
        for (int i = 0; i < dataBeanList.size(); i ++) {
            if (dataBeanList.get(i).isSelect() == true) {
                if (i == dataBeanList.size() - 1) {
                    delIDS += dataBeanList.get(i).getId();
                }
                else {
                    delIDS += dataBeanList.get(i).getId() + ",";
                }
            }
        }
        return delIDS;
    }
}
