package com.miyatu.mirror;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.miyatu.mirror.bean.UserDatabean;
import com.google.gson.Gson;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class PublicFragment<T> extends Fragment {

    protected int page = 1;
    protected int pageSize = 10;
    protected boolean isRefreshing = false;
    protected boolean isLoadMore = false;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), null);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Object object){

    }

    public UserDatabean.DataBean getUserDataBean() {
        String userJson = PreferencesUtils.getString(getActivity(), PreferencesUtils.USERDATA, "");
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

    protected void setUserData(UserDatabean userData) {
        if (userData != null) {
            PreferencesUtils.putString(getActivity(), PreferencesUtils.USERDATA, new Gson().toJson(userData));
        }
    }

    public void setTempData(int position) {
        this.position = position;
    }

    public int getTempData() {
        return position;
    }


//    protected void showFragment(Fragment fragment){
//        FragmentUtils.showFragment(getActivity(),fragment);
//    }

    protected abstract int getLayout();

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void updateView();

    protected void updateView(T t) {

    }
}
