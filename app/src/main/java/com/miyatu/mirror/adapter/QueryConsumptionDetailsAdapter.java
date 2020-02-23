package com.miyatu.mirror.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.QueryConsumptionDetailsBean;

import java.util.List;

public class QueryConsumptionDetailsAdapter extends BaseQuickAdapter<QueryConsumptionDetailsBean.DataBean, BaseViewHolder> {

    public QueryConsumptionDetailsAdapter(int layoutResId, @Nullable List<QueryConsumptionDetailsBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryConsumptionDetailsBean.DataBean item) {
        String name = item.getItem_name();
        switch (item.getItem_type()) {
            case 0:
                name = name + "-未知";
                break;
            case 1:
                name = name + "-升级会员";
                break;
            case 2:
                name = name + "-量体数据";
                break;
            case 3:
                name = name + "-余额充值";
                break;
        }
        switch (item.getPay_type()) {
            case 0:
                name = name + "-未知";
                break;
            case 1:
                name = name + "-支付宝支付";
                break;
            case 2:
                name = name + "-微信支付";
                break;
            case 3:
                name = name + "-余额支付";
                break;
        }
        helper.setText(R.id.tv_name, name);

        helper.setText(R.id.tv_date, item.getPay_time());
        helper.setText(R.id.tv_amount, item.getPay_money());
    }
}
