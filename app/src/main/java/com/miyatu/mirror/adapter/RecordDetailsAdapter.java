package com.miyatu.mirror.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.RecordDetailsBean;

import java.util.List;

public class RecordDetailsAdapter extends BaseQuickAdapter<RecordDetailsBean.DataBean.MeasureBean, BaseViewHolder> {
    public RecordDetailsAdapter(int layoutResId, @Nullable List<RecordDetailsBean.DataBean.MeasureBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordDetailsBean.DataBean.MeasureBean item) {
        helper.setText(R.id.tv_name, item.getName() + ":");
        helper.setText(R.id.tv_value, item.getValue() + item.getUnit());
    }
}
