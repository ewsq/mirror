package com.miyatu.mirror.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.activity.AddAccountBind;
import com.miyatu.mirror.activity.CourseListBean;
import com.miyatu.mirror.activity.FrontCameraActivity;
import com.miyatu.mirror.adapter.TakePhotoFragmentAdapter;
import com.miyatu.mirror.bean.AccountBindBean;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.miyatu.mirror.util.ScreenUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 拍照fragment
 * 布局视图：takephotofragment
 */
public class TakePhotoFragment extends PublicFragment implements View.OnClickListener, HttpOnNextListener {

    private LinearLayout ll_self_takephoto = null;//自拍模式
    private LinearLayout ll_help_takephoto = null;//帮拍模式

    private ViewPager mViewpager;
    private ViewPagerAdapter adapter;
    private ViewPagerIndicator mIndicatorDefault;//圆点指示器
    private List<Fragment> fragmentList = null;//存放 帮拍教程（fragment） 的list


    private JZVideoPlayerStandard videoPlayerFront ;
    private JZVideoPlayerStandard videoPlayerSide;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private List<CourseListBean.DataBean> dataBeanList = new ArrayList<>();
    private List<AccountBindBean.DataBean> addAccountBindList = new ArrayList<>();

    private PopupWindow popupWindow;
    private RecyclerView popRecyclerView;
    private TakePhotoFragmentAdapter popAdapter;

    private int cameraFacing;           //标记前后摄像头

    private void initRequest() {
        params = new HashMap<>();
        manager = new HttpManager(this,(RxAppCompatActivity)getActivity());
        api = new IndexApi(IndexApi.COURSE_LIST);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initRelativeListRequest() {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        manager = new HttpManager(this,(RxAppCompatActivity) getActivity());
        api = new IndexApi(IndexApi.RELATIVE_LIST);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    @Override
    protected int getLayout() {
        return R.layout.takephotofragment;
    }

    @Override
    protected void initView(View view) {
//        mViewpager = (ViewPager) view.findViewById(R.id.viewpager);
//        mIndicatorDefault = view.findViewById(R.id.indicator_default);

        ll_self_takephoto = view.findViewById(R.id.ll_self_takephoto);
        ll_self_takephoto.setOnClickListener(this);
        ll_help_takephoto = view.findViewById(R.id.ll_help_takephoto);
        ll_help_takephoto.setOnClickListener(this);

        videoPlayerFront = view.findViewById(R.id.videoPlayerFront);
        videoPlayerSide = view.findViewById(R.id.videoPlayerSide);
    }

    @Override
    protected void initData() {
        initRequest();
        initRelativeListRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            GalleryFragment galleryFragment = new GalleryFragment(dataBeanList.get(i));
            fragmentList.add(galleryFragment);
        }

        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
        mViewpager.setAdapter(adapter);
        mViewpager.setCurrentItem(0);
        mIndicatorDefault.setViewPager(mViewpager);
    }


    /*监听fragment的可见性*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            showDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_self_takephoto: {                  //自拍模式
                cameraFacing = MyApp.FACING_FRONT;
                initPopWindow(v, cameraFacing);
                break;
            }
            case R.id.ll_help_takephoto: {
                cameraFacing = MyApp.FACING_BACK;        //帮拍模式
                initPopWindow(v, cameraFacing);
                break;
            }
        }

    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.COURSE_LIST)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            CourseListBean data = new Gson().fromJson(resulte, new TypeToken<CourseListBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                dataBeanList = data.getData();

                videoPlayerFront.setUp(MyApp.imageUrl + dataBeanList.get(0).getVideo(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);              //设置视频地址
                Glide.with(this).load(MyApp.imageUrl + dataBeanList.get(0).getCover()).placeholder( R.mipmap.edit_person_info_headicon).       //设置预览图
                        into(videoPlayerFront.thumbImageView);

                videoPlayerSide.setUp(MyApp.imageUrl + dataBeanList.get(1).getVideo(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);              //设置视频地址
                Glide.with(this).load(MyApp.imageUrl + dataBeanList.get(1).getCover()).placeholder( R.mipmap.edit_person_info_headicon).       //设置预览图
                        into(videoPlayerSide.thumbImageView);


//                initViewPager();
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.RELATIVE_LIST)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            AccountBindBean data = new Gson().fromJson(resulte, new TypeToken<AccountBindBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                addAccountBindList = data.getData();
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

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private FragmentManager mSupportFragmentManager;
        private List<Fragment> mFragmentList = null;

        public ViewPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> fragmentList) {
            super(supportFragmentManager);
            mFragmentList = fragmentList;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


    private void initPopWindow(View v, int cameraFacing) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_volume_tip,null);

        LinearLayout ll_dialog_haved_account = view.findViewById(R.id.ll_dialog_haved_account);
        LinearLayout ll_dialog_new_set_account = view.findViewById(R.id.ll_dialog_new_set_account);

        TextView tv_dialog_haved_account = view.findViewById(R.id.tv_dialog_haved_account);
        ImageView iv_dialog_haved_account = view.findViewById(R.id.iv_dialog_haved_account);
        if (getUserDataBean().getRelative().getGender() == 0) {
            iv_dialog_haved_account.setVisibility(View.INVISIBLE);
        }
        else if (getUserDataBean().getRelative().getGender() == 1) {
            iv_dialog_haved_account.setImageResource(R.mipmap.new_volume_tiphead_icon);
        }
        else {
            iv_dialog_haved_account.setImageResource(R.mipmap.new_valume_tiphead_red);
        }
        tv_dialog_haved_account.setText(getUserDataBean().getRelative().getRelative());

        int width = ScreenUtils.getScreenWidth(getActivity());
        int height = ScreenUtils.getScreenHeight(getActivity());
        popupWindow = new PopupWindow(view, width*3/4, height/2);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(getActivity(),0.5f,1f);       //屏幕亮度变化
            }
        });

        popRecyclerView = view.findViewById(R.id.recyclerView);
        popAdapter = new TakePhotoFragmentAdapter(R.layout.item_pop_take_photo_fragment, addAccountBindList);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        popRecyclerView.setNestedScrollingEnabled(false);
        popRecyclerView.setAdapter(popAdapter);
        popAdapter.setOnItemChildClickListener(new TakePhotoFragmentAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AccountBindBean.DataBean dataBean = popAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.linearLayout:
                        Bundle bundle = new Bundle();
                        bundle.putInt("apiType", MyApp.PROFILE2MEASURE);
                        bundle.putString("userName", dataBean.getRelative());
                        bundle.putInt("gender", dataBean.getGender());
                        bundle.putString("height", dataBean.getHeight());
                        bundle.putString("weight", dataBean.getWeight());
                        bundle.putString("relativeID", String.valueOf(dataBean.getId()));
                        bundle.putInt("cameraFacing", cameraFacing);
                        startActivity(new Intent(getActivity(), FrontCameraActivity.class).putExtras(bundle));
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        ll_dialog_haved_account.setOnClickListener(new View.OnClickListener() {             //选择了已有量体账户
            @Override
            public void onClick(View v) {
                UserDatabean.DataBean.RelativeBean relativeBean = getUserDataBean().getRelative();
                Bundle bundle = new Bundle();
                bundle.putInt("apiType", MyApp.PROFILE2MEASURE);
                bundle.putString("userName", relativeBean.getRelative());
                bundle.putInt("gender", relativeBean.getGender());
                bundle.putString("height", relativeBean.getHeight());
                bundle.putString("weight", relativeBean.getWeight());
                bundle.putString("relativeID", String.valueOf(relativeBean.getRelative_id()));
                bundle.putInt("cameraFacing", cameraFacing);

                startActivity(new Intent(getActivity(), FrontCameraActivity.class).putExtras(bundle));
                popupWindow.dismiss();
            }
        });
        ll_dialog_new_set_account.setOnClickListener(new View.OnClickListener() {               //新建量体账户
            @Override
            public void onClick(View v) {
                AddAccountBind.startActivity(getActivity());
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        ScreenUtils.dimBackground(getActivity(), 1f, 0.5f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object o) {
        initRelativeListRequest();
    }

}
