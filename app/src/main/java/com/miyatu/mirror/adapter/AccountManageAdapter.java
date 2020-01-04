package com.miyatu.mirror.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.AccountBindBean;

import java.util.List;

public class AccountManageAdapter extends BaseQuickAdapter<AccountBindBean.DataBean, BaseViewHolder> {
    public AccountManageAdapter(int layoutResId, @Nullable List<AccountBindBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountBindBean.DataBean item) {
        ImageView imageView = helper.getView(R.id.iv_sex);
        if (item.getGender() == 0) {
            imageView.setVisibility(View.INVISIBLE);
        }
        else if (item.getGender() == 1) {
            imageView.setImageResource(R.mipmap.man_select);
        }
        else {
            imageView.setImageResource(R.mipmap.woman_select);
        }

        helper.setText(R.id.tv_name, item.getRelative());
        helper.setText(R.id.tv_height, item.getHeight() + "cm");

        ImageView ivSelect = helper.getView(R.id.iv_select);
        ivSelect.setImageResource(item.isSelect() == true ? R.mipmap.selected : R.mipmap.unselected);
        helper.addOnClickListener(R.id.iv_select);

        helper.addOnClickListener(R.id.iv_edit);
    }
}
