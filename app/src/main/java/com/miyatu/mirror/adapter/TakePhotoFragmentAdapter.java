package com.miyatu.mirror.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myapplication.R;
import com.miyatu.mirror.bean.AccountBindBean;

import java.util.List;

public class TakePhotoFragmentAdapter extends BaseQuickAdapter<AccountBindBean.DataBean, BaseViewHolder> {

    public TakePhotoFragmentAdapter(int layoutResId, @Nullable List<AccountBindBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountBindBean.DataBean item) {
        ImageView imageView = helper.getView(R.id.imageView);
        if (item.getGender() == 0) {
            imageView.setVisibility(View.INVISIBLE);
        }
        else if (item.getGender() == 1) {
            imageView.setImageResource(R.mipmap.new_volume_tiphead_icon);
        }
        else {
            imageView.setImageResource(R.mipmap.new_valume_tiphead_red);
        }

        helper.setText(R.id.textView, item.getRelative());

        helper.addOnClickListener(R.id.linearLayout);
    }
}
