package com.miyatu.mirror.fragment;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.activity.CourseListBean;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 帮拍教程的fragment
 */
public class GalleryFragment extends PublicFragment {
    private TextView tvTitle;
    private TextView tvSimple;
    private JZVideoPlayerStandard jzVideoPlayerStandard ;
    private CourseListBean.DataBean dataBean;

    private boolean mIsExit;

    public GalleryFragment(CourseListBean.DataBean dataBean){
        this.dataBean = dataBean;
    }

    @Override
    protected int getLayout() {
        return R.layout.galleryfragment;
    }

    @Override
    protected void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvSimple = view.findViewById(R.id.tv_simple);

        jzVideoPlayerStandard = view.findViewById(R.id.videoPlayer);
    }

    @Override
    protected void initData() {
        tvTitle.setText(dataBean.getTitle());
        tvSimple.setText(dataBean.getSimple());

        jzVideoPlayerStandard.setUp(MyApp.imageUrl + dataBean.getVideo(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);              //设置视频地址
        Glide.with(this).load(MyApp.imageUrl + dataBean.getCover()).placeholder( R.mipmap.edit_person_info_headicon).       //设置预览图
                into(jzVideoPlayerStandard.thumbImageView);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }
}
