package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.AccountBindBean;
import com.miyatu.mirror.bean.AddAccountBindBean;
import com.miyatu.mirror.bean.BaseBean;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.LogUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAccountBind extends PublicActivity implements HttpOnNextListener{
    private RelativeLayout rlBack;
    private TextView title;
    private TextView save;

    private RadioGroup radioGroupSex;
    private EditText etNickName;
    private TextView etHeight;
    private TextView etWeight;
    private LinearLayout linSex;

    private AccountBindBean.DataBean dataBean;
    private int sex = 0;
    private float height;
    private float weight;
    private String nickName;

    private int cameraFacing;

    public final static int IS_OTHER_ACCOUNT = 0;           //亲友量体账户
    public final static int IS_MY_ACCOUNT = 1;              //我的量体账户
    private int tag;                    //标记是否是我的量体账户

//    private PopupWindow popHeight;
//    private PopupWindow popWeight;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private void initEditRequest(int id, String relative, float height, float weight) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("relative_id", id);
        params.put("relative", relative);
        params.put("height", height);
        params.put("weight", weight);
        manager = new HttpManager(this,(RxAppCompatActivity)AddAccountBind.this);
        api = new IndexApi(IndexApi.EDIT_RELATIVE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    private void initAddRequest(String relative, int gender, float height, float weight) {
        params = new HashMap<>();
        params.put("token", getUserDataBean().getToken());
        params.put("relative", relative);
        params.put("gender", gender);
        params.put("height", height);
        params.put("weight", weight);
        manager = new HttpManager(this,(RxAppCompatActivity)AddAccountBind.this);
        api = new IndexApi(IndexApi.ADD_RELATIVE);
        api.setParams(params);
        manager.doHttpDeal(api);
    }

    public static void startActivity(Context context, AccountBindBean.DataBean dataBean, int tag){
        Intent intent = new Intent(context, AddAccountBind.class);
        intent.putExtra("dataBean", dataBean);
        intent.putExtra("tag", tag);
        context.startActivity(intent);
    }
    public static void startActivity(Context context){
        Intent intent = new Intent(context, AddAccountBind.class);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, int cameraFacing){
        Intent intent = new Intent(context, AddAccountBind.class);
        intent.putExtra("cameraFacing", cameraFacing);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_add_account_bind;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        title = findViewById(R.id.title);
        etNickName = findViewById(R.id.et_nickName);
        radioGroupSex = findViewById(R.id.registe_radiogroup_sex);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        save = findViewById(R.id.save);
        linSex = findViewById(R.id.linSex);

//        initHeightPop();
//        initWeightPop();
    }

    @Override
    protected void initData() {
        dataBean = getIntent().getParcelableExtra("dataBean");

        sex = 1;        //性别默认为男性
        radioGroupSex.check(R.id.registe_radio_sex_man);

        if (dataBean == null) {
            title.setText("新增量体账户");
            linSex.setVisibility(View.VISIBLE);
        }
        else {
            title.setText("修改量体账户");
            linSex.setVisibility(View.GONE);

            height = Float.parseFloat(dataBean.getHeight());
            weight = Float.parseFloat(dataBean.getWeight());
            sex = dataBean.getGender();

            etNickName.setText(dataBean.getRelative());
            etHeight.setText(height + "");
            etWeight.setText(weight + "");
        }

        tag = getIntent().getIntExtra("tag", 0);

    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.registe_radio_sex_man:
                        sex = 1;
                        break;
                    case R.id.registe_radio_sex_woman:
                        sex = 2;
                        break;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName = etNickName.getText().toString().trim();
                String strHeight = etHeight.getText().toString().trim();
                String strWeight = etWeight.getText().toString().trim();
                if (nickName == null || nickName.equals("")) {
                    ToastUtils.show("昵称不能为空");
                    return;
                }
                if (strHeight == null || strHeight.equals("")) {
                    ToastUtils.show("身高不能为空");
                    return;
                }
                else {
                    if (isNumeric(strHeight) == false) {
                        ToastUtils.show("身高必须为数字");
                        return;
                    }
                    else {
                        height = Float.parseFloat(strHeight);
                    }
                }
                if (strWeight == null || strWeight.equals("")) {
                    ToastUtils.show("体重不能为空");
                    return;
                }
                else{
                    if (isNumeric(strWeight) == false) {
                        ToastUtils.show("体重必须为数字");
                        return;
                    }
                    else {
                        weight = Float.parseFloat(strWeight);
                    }
                }
                if (dataBean == null) {
                    initAddRequest(etNickName.getText().toString().trim(), sex, height, weight);
                }
                else {
                    initEditRequest(dataBean.getId(), etNickName.getText().toString(), height, weight);
                }
            }
        });
    }

    @Override
    protected void updateView() {

    }


    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.EDIT_RELATIVE)) {
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            BaseBean data = new Gson().fromJson(resulte, new TypeToken<BaseBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                if(tag == IS_MY_ACCOUNT) {
                    UserDatabean.DataBean dataBean = getUserDataBean();
                    UserDatabean.DataBean.RelativeBean relativeBean = dataBean.getRelative();
                    relativeBean.setGender(sex);
                    relativeBean.setHeight(String.valueOf(height));
                    relativeBean.setWeight(String.valueOf(weight));
                    relativeBean.setRelative(nickName);
                    setUserData(dataBean);
                }
                EventBus.getDefault().post(data);
                finish();
                return;
            }
            ToastUtils.show(data.getMsg());
        }
        if (mothead.equals(IndexApi.ADD_RELATIVE)) {
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            AddAccountBindBean data = new Gson().fromJson(resulte, new TypeToken<AddAccountBindBean>(){}.getType());
            if (data.getStatus() == 1) {          //成功
                EventBus.getDefault().post(data);

                Bundle bundle = new Bundle();
                bundle.putInt("apiType", MyApp.PROFILE2MEASURE);
                bundle.putString("userName", etNickName.getText().toString().trim());
                bundle.putInt("gender", sex);
                bundle.putString("height", etHeight.getText().toString().trim());
                bundle.putString("weight", etWeight.getText().toString().trim());
                bundle.putString("relativeID", data.getData());
                bundle.putInt("cameraFacing", cameraFacing);
                startActivity(new Intent(AddAccountBind.this, FrontCameraActivity.class).putExtras(bundle));

                finish();

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

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

//    private void initHeightPop() {
//        View view = LayoutInflater.from(AddAccountBind.this).inflate(R.layout.pop_num_picker,null);
//        popHeight = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        popHeight.setOutsideTouchable(true);
//        popHeight.setClippingEnabled(false);
//        popHeight.setFocusable(true);
//
//        popHeight.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
//            @Override
//            public void onDismiss() {
//                ScreenUtils.dimBackground(AddAccountBind.this,0.5f,1f);       //屏幕亮度变化
//            }
//        });
//
//        NumberPicker numberPicker = view.findViewById(R.id.numPicker);
//        TextView text = view.findViewById(R.id.text);
//        TextView tvSure = view.findViewById(R.id.tv_sure);
//
//        text.setText("cm");
//        //设置最大值
//        numberPicker.setMaxValue(300);
//        //设置最小值
//        numberPicker.setMinValue(1);
//        //设置当前值
//        numberPicker.setValue(Double.valueOf(tvHeight.getText().toString()).intValue());
//        //设置滑动监听
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            //当NunberPicker的值发生改变时，将会激发该方法
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                height = newVal;
//            }
//        });
//        tvSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvHeight.setText(height + "");
//                popHeight.dismiss();
//            }
//        });
//    }
//
//    private void initWeightPop() {
//        View view = LayoutInflater.from(AddAccountBind.this).inflate(R.layout.pop_num_picker,null);
//        popWeight = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        popWeight.setOutsideTouchable(true);
//        popWeight.setClippingEnabled(false);
//        popWeight.setFocusable(true);
//
//        popWeight.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
//            @Override
//            public void onDismiss() {
//                ScreenUtils.dimBackground(AddAccountBind.this,0.5f,1f);       //屏幕亮度变化
//            }
//        });
//
//        NumberPicker numberPicker = view.findViewById(R.id.numPicker);
//        TextView text = view.findViewById(R.id.text);
//        TextView tvSure = view.findViewById(R.id.tv_sure);
//
//        text.setText("kg");
//        //设置最大值
//        numberPicker.setMaxValue(300);
//        //设置最小值
//        numberPicker.setMinValue(1);
//        //设置当前值
//        numberPicker.setValue(Double.valueOf(tvWeight.getText().toString()).intValue());
//        //设置滑动监听
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            //当NunberPicker的值发生改变时，将会激发该方法
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                weight = newVal;
//            }
//        });
//        tvSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvWeight.setText(weight + "");
//                popWeight.dismiss();
//            }
//        });
//    }

}
