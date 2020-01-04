package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.adapter.AccountBindAdapter;
import com.miyatu.mirror.bean.AccountBindBean;
import com.miyatu.mirror.http.api.IndexApi;
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

//账号绑定特面
public class AccountBind extends PublicActivity implements View.OnClickListener, HttpOnNextListener {
    private  List userList;//新建账户bean
    private ImageView iv_bind_back;//返回
    private TextView tv_manage;//管理
    private ImageView iv_bind_sex;//性別
    private ImageView iv_bind_edit;//编辑
    private TextView tv_bind_name;//姓名
    private TextView tv_bind_height;//身高
    private LinearLayout ll_bind_add;//添加新账户

    private RecyclerView recyclerView;
    private AccountBindAdapter adapter;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        manager = new HttpManager(this,(RxAppCompatActivity)AccountBind.this);
        api = new IndexApi(IndexApi.RELATIVE_LIST);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AccountBind.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_bind;
    }

    protected void initView() {
        recyclerView=findViewById(R.id.lv_new_account_info);

        iv_bind_back=findViewById(R.id.iv_bind_back);
        iv_bind_back.setOnClickListener(this);
        tv_manage=findViewById(R.id.tv_manage);
        tv_manage.setOnClickListener(this);
        iv_bind_sex=findViewById(R.id.iv_bind_sex);
        iv_bind_edit=findViewById(R.id.iv_bind_edit);
        iv_bind_edit.setOnClickListener(this);
        tv_bind_name=findViewById(R.id.tv_bind_name);
        tv_bind_height=findViewById(R.id.tv_bind_height);
        ll_bind_add=findViewById(R.id.ll_bind_add);
        ll_bind_add.setOnClickListener(this);

        initRecyclerView();
    }

    protected void initData() {
        initMyAccount();
        initRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initRecyclerView() {
        adapter = new AccountBindAdapter(R.layout.lv_item, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(AccountBind.this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new AccountBindAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AccountBindBean.DataBean dataBean = (AccountBindBean.DataBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_edit:
                        AddAccountBind.startActivity(AccountBind.this, dataBean, AddAccountBind.IS_OTHER_ACCOUNT);
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
        switch (v.getId()){
            case R.id.iv_bind_back:{
                finish();
                break;
            }
            case R.id.tv_manage:{
                AccountManage.startActivity(AccountBind.this);
                break;
            }
            case R.id.iv_bind_edit:{
                AccountBindBean.DataBean dataBean = new AccountBindBean.DataBean();
                dataBean.setRelative(getUserDataBean().getRelative().getRelative());
                dataBean.setId(getUserDataBean().getRelative().getRelative_id());
                dataBean.setGender(getUserDataBean().getRelative().getGender());
                dataBean.setHeight(getUserDataBean().getRelative().getHeight());
                dataBean.setWeight(getUserDataBean().getRelative().getWeight());
                AddAccountBind.startActivity(AccountBind.this, dataBean, AddAccountBind.IS_MY_ACCOUNT);
                break;
            }
            case R.id.ll_bind_add:{
                AddAccountBind.startActivity(AccountBind.this);
                break;
            }
        }

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.RELATIVE_LIST)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            AccountBindBean data = new Gson().fromJson(resulte, new TypeToken<AccountBindBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                adapter.setNewData(data.getData());
                return;
            }
            ToastUtils.show(data.getMsg());
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.i(e.getMessage());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object o) {
        initMyAccount();

        recyclerView.removeAllViews();          //防止recyclerView绑定新的数据时错乱
        initRequest();
    }
}
