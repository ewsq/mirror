package com.miyatu.mirror.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.miyatu.mirror.bean.FriendVolumeRecordFragmentBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yhd on 2019/11/21.
 * Email 1443160259@qq.com
 */

//好友记录列表适配器

public class FriendVolumeAdapter extends BaseQuickAdapter<FriendVolumeRecordFragmentBean.DataBean, BaseViewHolder> {

    public FriendVolumeAdapter(int layoutResId, @Nullable List<FriendVolumeRecordFragmentBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendVolumeRecordFragmentBean.DataBean item) {
        if (item.getIs_pay() == 0) {            //是否已付款，0未付款，1已付款
            helper.getView(R.id.linUserData).setVisibility(View.GONE);
            helper.getView(R.id.linPay).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_more).setVisibility(View.GONE);
            helper.getView(R.id.tv_number_pay).setVisibility(View.VISIBLE);
        }
        else {
            helper.getView(R.id.linUserData).setVisibility(View.VISIBLE);
            helper.getView(R.id.linPay).setVisibility(View.GONE);
            helper.getView(R.id.tv_more).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_number_pay).setVisibility(View.GONE);
        }

        ImageView ivSex = helper.getView(R.id.iv_sex);
        Glide.with(mContext).load(item.getGender() == 1? R.mipmap.icon_men : R.mipmap.icon_women).placeholder( R.mipmap.edit_person_info_headicon).into(ivSex);

        helper.setText(R.id.tv_name, item.getRelative());
        helper.setText(R.id.tv_height, item.getHeight());
        helper.setText(R.id.tv_date, getDateToString(item.getCreate_time()));
        helper.setText(R.id.tv_number_pay, "¥" + item.getPrice());

        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        ChildAdapter childAdapter = new ChildAdapter(R.layout.item_item_friend_volume_record_fragment, item.getMeasure());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(childAdapter);

        helper.addOnClickListener(R.id.tv_pay);
        helper.addOnClickListener(R.id.tv_more);
    }

    class ChildAdapter extends BaseQuickAdapter<FriendVolumeRecordFragmentBean.DataBean.MeasureBean, BaseViewHolder> {

        public ChildAdapter(int layoutResId, @Nullable List<FriendVolumeRecordFragmentBean.DataBean.MeasureBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FriendVolumeRecordFragmentBean.DataBean.MeasureBean item) {
            helper.setText(R.id.tv_name, item.getName() + ":");
            helper.setText(R.id.tv_value, item.getValue() + item.getUnit());
        }
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond* 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
