package com.miyatu.mirror.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.MeasurementBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMeasurementAdapter extends BaseQuickAdapter<MeasurementBean, BaseViewHolder> {
    public AddMeasurementAdapter(int layoutResId, @Nullable List<MeasurementBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasurementBean item) {
        helper.setText(R.id.tv_name, item.getName());

        helper.setText(R.id.tv_value, replaceValue(item.getValue()) + item.getUnit());
    }

    private StringBuilder replaceValue(String value) {
        StringBuilder sb = new StringBuilder();
        if(!TextUtils.isEmpty(value)){
            if (isNumeric(value)) {
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    if (i >= 1) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
            }
            else {
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    sb.append(c);
                }
            }
        }
        return sb;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
