package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.bean.EditInfoBean;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.http.api.IndexApi;
import com.miyatu.mirror.util.BitmapUtil;
import com.miyatu.mirror.util.LogUtils;
import com.miyatu.mirror.util.ScreenUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PersonInfo extends PublicActivity implements View.OnClickListener, HttpOnNextListener {
    private TextView pick_height;
    private TextView et_show_name;
    private TextView tv_show_sex;
    private TextView tv_show_birthday;
    private TextView et_show_email;
    private TextView pick_weight;
    private TextView tv_save;
    private ImageView iv_back;
    private ImageView iv_change_head;
    private ImageView iv_show_sex_icon;
    private LinearLayout linHeight;
    private LinearLayout linWeight;
    private LinearLayout linSex;

    private PopupWindow popDate;
    private PopupWindow popHeight;
    private PopupWindow popWeight;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int sex;        //1为男，2为女
    private int height;
    private int weight;
    private File portraitFile;
    private String portraitUrl;
    private String birthday;
    private String email;

    private HttpManager manager;
    private IndexApi api;
    private Map<String, Object> params;

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PersonInfo.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }

    protected void initView() {
        pick_height=findViewById(R.id.pick_height);
        pick_height.setOnClickListener(this);
        et_show_name=findViewById(R.id.et_show_name);
        tv_show_sex=findViewById(R.id.tv_show_sex);
        tv_show_sex.setOnClickListener(this);
        tv_show_birthday=findViewById(R.id.tv_show_birthday);
        tv_show_birthday.setOnClickListener(this);
        et_show_email = findViewById(R.id.et_show_email);
        tv_save=findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_change_head=findViewById(R.id.iv_change_head);
        iv_change_head.setOnClickListener(this);
        iv_show_sex_icon=findViewById(R.id.iv_show_sex_icon);
        pick_weight=findViewById(R.id.pick_weight);
        pick_weight.setOnClickListener(this);
        linHeight = findViewById(R.id.linHeight);
        linHeight.setOnClickListener(this);
        linWeight = findViewById(R.id.linWeight);
        linWeight.setOnClickListener(this);
        linSex = findViewById(R.id.linSex);
        linSex.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        calendar = Calendar.getInstance();

        initUserData();

        initHeightPop();
        initWeightPop();
        initDatePickerPop();

        initRequest();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initUserData() {
        params = new HashMap<>();

        sex = getUserDataBean().getSex();
        weight = Double.valueOf(getUserDataBean().getRelative().getWeight()).intValue();
        height = Double.valueOf(getUserDataBean().getRelative().getHeight()).intValue();
        portraitUrl = getUserDataBean().getHead_pic();
        birthday = getUserDataBean().getBirthday();
        email = getUserDataBean().getEmail();

        et_show_name.setText(getUserDataBean().getNickname());
        Glide.with(this).load(MyApp.imageUrl + portraitUrl).placeholder( R.mipmap.edit_person_info_headicon).into(iv_change_head);
        if (sex == 1) {
            iv_show_sex_icon.setImageResource(R.mipmap.man_select);
            tv_show_sex.setText("男");
        }
        else {
            iv_show_sex_icon.setImageResource(R.mipmap.woman_select);
            tv_show_sex.setText("女");
        }
        pick_height.setText(height + "");
        pick_weight.setText(weight + "");
        tv_show_birthday.setText(birthday);
        et_show_email.setText(email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linHeight:{
                ScreenUtils.dimBackground(PersonInfo.this,1f,0.5f);       //屏幕亮度变化
                popHeight.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.linWeight:{
                ScreenUtils.dimBackground(PersonInfo.this,1f,0.5f);       //屏幕亮度变化
                popWeight.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.tv_show_birthday: {
                ScreenUtils.dimBackground(PersonInfo.this,1f,0.5f);       //屏幕亮度变化
                popDate.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.linSex: {
                if (sex == 1) {
                    sex = 2;
                    iv_show_sex_icon.setImageResource(R.mipmap.woman_select);
                    tv_show_sex.setText("女");
                } else {
                    sex = 1;
                    iv_show_sex_icon.setImageResource(R.mipmap.man_select);
                    tv_show_sex.setText("男");
                }
                break;
            }
            case R.id.tv_save:{
                api.setNickName(et_show_name.getText().toString());
                api.setGender(String.valueOf(sex));
                api.setHeight(pick_height.getText().toString().trim());
                api.setWeight(pick_weight.getText().toString().trim());
                api.setBirthday(tv_show_birthday.getText().toString().trim());
                api.setEmail(et_show_email.getText().toString().trim());
                if (portraitFile != null) {
                    api.setHeadImage(portraitFile);
                }
                else {
                    api.setHeadImageUri(getUserDataBean().getHead_pic());
                }
                manager.doHttpDeal(api);
                break;
            } case R.id.iv_back:{
                finish();
                break;
            } case R.id.iv_change_head:{
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;
            }
        }
    }

    private void initRequest() {
        manager = new HttpManager(this,(RxAppCompatActivity)this);
        api = new IndexApi(IndexApi.EDIT_INFO);
        api.setToken(getUserDataBean().getToken());
    }

    private void initHeightPop() {
        View view = LayoutInflater.from(PersonInfo.this).inflate(R.layout.pop_num_picker,null);
        popHeight = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeight.setFocusable(true);
        popHeight.setTouchable(true);
        popHeight.setOutsideTouchable(false);
        popHeight.setClippingEnabled(false);

        popHeight.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(PersonInfo.this,0.5f,1f);       //屏幕亮度变化
            }
        });

        NumberPicker numberPicker = view.findViewById(R.id.numPicker);
        TextView text = view.findViewById(R.id.text);
        TextView tvSure = view.findViewById(R.id.tv_sure);

        text.setText("cm");
        //设置最大值
        numberPicker.setMaxValue(300);
        //设置最小值
        numberPicker.setMinValue(1);
        //设置当前值
        numberPicker.setValue(Double.valueOf(pick_height.getText().toString()).intValue());
        //设置滑动监听
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            //当NunberPicker的值发生改变时，将会激发该方法
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                height = newVal;
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick_height.setText(height + "");
                popHeight.dismiss();
            }
        });
    }

    private void initWeightPop() {
        View view = LayoutInflater.from(PersonInfo.this).inflate(R.layout.pop_num_picker,null);
        popWeight = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popWeight.setFocusable(true);
        popWeight.setTouchable(true);
        popWeight.setOutsideTouchable(false);
        popWeight.setClippingEnabled(false);

        popWeight.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(PersonInfo.this,0.5f,1f);       //屏幕亮度变化
            }
        });

        NumberPicker numberPicker = view.findViewById(R.id.numPicker);
        TextView text = view.findViewById(R.id.text);
        TextView tvSure = view.findViewById(R.id.tv_sure);

        text.setText("kg");
        //设置最大值
        numberPicker.setMaxValue(300);
        //设置最小值
        numberPicker.setMinValue(1);
        //设置当前值
        numberPicker.setValue(Double.valueOf(pick_weight.getText().toString()).intValue());
        //设置滑动监听
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            //当NunberPicker的值发生改变时，将会激发该方法
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                weight = newVal;
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick_weight.setText(weight + "");
                popWeight.dismiss();
            }
        });
    }

    private void initDatePickerPop() {
        View datePickerView = LayoutInflater.from(PersonInfo.this).inflate(R.layout.pop_date_picker,null);
        popDate = new PopupWindow(datePickerView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popDate.setFocusable(true);
        popDate.setTouchable(true);
        popDate.setOutsideTouchable(false);
        popDate.setClippingEnabled(false);

        popDate.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(PersonInfo.this,0.5f,1f);       //屏幕亮度变化
            }
        });

        DatePicker datePicker = datePickerView.findViewById(R.id.datePicker);
        TextView tvSure = datePickerView.findViewById(R.id.tv_sure);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {         //datePicker滚动监听
            @Override
            public void onDateChanged(DatePicker view, int changedYear, int changedMonth, int changedDay) {
                year = changedYear;
                month = changedMonth;
                day = changedDay;
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthday = year + "-" + (month + 1) + "-" + day;
                tv_show_birthday.setText(birthday);
                popDate.dismiss();
            }
        });
    }

    @Override
    public void onNext(String resulte, String mothead) {
        if (mothead.equals(IndexApi.EDIT_INFO)) {
            LogUtils.i(resulte);
            //fromJson()方法来实现从Json相关对象到Java实体。TypeToken是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
            EditInfoBean data = new Gson().fromJson(resulte, new TypeToken<EditInfoBean>(){}.getType());
            if (data.getStatus() == 1) {
                UserDatabean.DataBean dataBean = getUserDataBean();
                dataBean.setHead_pic(data.getData().getHead_pic());
                dataBean.setSex(data.getData().getGender());
                dataBean.setBirthday(data.getData().getBirthday());
                dataBean.setNickname(data.getData().getNickname());
                dataBean.setEmail(data.getData().getEmail());
                dataBean.getRelative().setHeight(data.getData().getHeight());
                dataBean.getRelative().setWeight(data.getData().getWeight());
                setUserData(dataBean);
                EventBus.getDefault().post(dataBean);
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

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        }
        else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                Glide.with(this).load(bitmap).into(iv_change_head);
                portraitFile = BitmapUtil.bitmapToFile(bitmap, "portrait");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}