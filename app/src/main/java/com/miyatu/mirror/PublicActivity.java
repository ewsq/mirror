package com.miyatu.mirror;

import android.os.Bundle;

import com.google.gson.Gson;
import com.miyatu.mirror.bean.UserDatabean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class PublicActivity<T> extends RxAppCompatActivity{
    protected boolean isRefreshing = false;
    protected boolean isLoadMore = false;
    protected int listRows = 20;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(getLayoutId());

        initView();
        initData();
        updateView();
        initEvent();
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public UserDatabean.DataBean getUserDataBean() {
        String userJson = PreferencesUtils.getString(this, PreferencesUtils.USERDATA, "");
        if (userJson == null || userJson.equals("")) {
            UserDatabean.DataBean dataBean = new UserDatabean.DataBean();
            dataBean.setNickname("");
            dataBean.setToken("");
            dataBean.setOpenid("");
            dataBean.setMobile("");
            dataBean.setSex(0);
            dataBean.setEmail("");
            dataBean.setBirthday("");
            dataBean.setHead_pic("");
            dataBean.setAlipay("");
            dataBean.setQq("");
            dataBean.setLast_login("");
            UserDatabean.DataBean.RelativeBean relativeBean = new UserDatabean.DataBean.RelativeBean();
            relativeBean.setRelative_id(0);
            relativeBean.setRelative("");
            relativeBean.setGender(0);
            relativeBean.setHeight("0");
            relativeBean.setWeight("0");
            dataBean.setRelative(relativeBean);
            return dataBean;
        }
        return new Gson().fromJson(userJson, UserDatabean.DataBean.class);
    }

    protected void setUserData(UserDatabean.DataBean dataBean) {
        if (dataBean != null) {
            PreferencesUtils.putString(this, PreferencesUtils.USERDATA, new Gson().toJson(dataBean));
        }
    }


    // 获取布局
    protected abstract int getLayoutId();

    // 初始化布局
    protected abstract void initView();

    // 初始化数据 大多用于加载页面的接口调用
    protected abstract void initData();

    // 初始化监听事件
    protected abstract void initEvent();

    // 修改页面 大多用于接口调用完毕更新view
    protected abstract void updateView();

    protected void updateView(T t) {

    }

    @Subscribe
    public void onEvent(Object object) {

    }
}
