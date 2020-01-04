package com.miyatu.mirror.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.R;

import java.util.Calendar;

/**
 * 注册页面，性别、生日
 */
public class RegisteSexbirthdayActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup radiogroup_sex;
    private TextView radiotext_sex_man=null,radiotext_sex_woman=null;
    private TextView tv_birthday=null;
    private Button btn_nextstep=null;
    private ImageView iv_back=null;

    private int selectSex = 0;       //保密 1男 2女

    private AlertDialog dialog = null;
    private  TextView dialog_tip_iknow=null;
    private PopupWindow popupWindow;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    private String mobile = "";
    private int code = 0;
    private String sessionId = "";
    private String password = "";
    private String nickName = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeinfo_sexbirthday);
        initView();
        showDialog();
    }

    private void initView() {
        radiogroup_sex=findViewById(R.id.registe_radiogroup_sex);
        radiogroup_sex.setOnCheckedChangeListener(this);
        radiotext_sex_man=findViewById(R.id.registe_radiotext_sex_man);
        radiotext_sex_woman=findViewById(R.id.registe_radiotext_sex_woman);
        tv_birthday=findViewById(R.id.tv_birthday);
        tv_birthday.setOnClickListener(this);
        btn_nextstep=findViewById(R.id.btn_registesexbirthday_nextstep);
        btn_nextstep.setOnClickListener(this);
        iv_back=findViewById(R.id.iv_registesexbirthday_back);
        iv_back.setOnClickListener(this);


        calendar = Calendar.getInstance();
        initPop();

        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getIntExtra("code", 0);
        password = getIntent().getStringExtra("password");
        sessionId = getIntent().getStringExtra("session_id");
        nickName = getIntent().getStringExtra("nickname");
        email = getIntent().getStringExtra("email");


        selectSex = 1;          //设置默认为男性
        changeRadiotextStatus();//初始化时获取 selectSex的值来改变字体状态
    }

    /**
     * 根据选择的radiobutton改变radiobutton_text的颜色
     */
    private void changeRadiotextStatus() {
        if (selectSex == 1){
            radiotext_sex_man.setText("男");
            radiotext_sex_woman.setHint("");
            radiotext_sex_woman.setText("");
            radiotext_sex_woman.setHint("女");
        }else {
            radiotext_sex_man.setText("");
            radiotext_sex_man.setHint("男");
            radiotext_sex_woman.setText("女");
            radiotext_sex_woman.setHint("");
        }
    }

    /**
     * 对话框提示
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showDialog() {
        // 构建dialog显示的view布局
        View view_dialog = getLayoutInflater().from(this).inflate(R.layout.tipcontent, null);
        dialog_tip_iknow=view_dialog.findViewById(R.id.dialog_tip_iknow);
        if (dialog == null) {
            // 创建AlertDialog对象
            dialog = new AlertDialog.Builder(this)
                    .create();
            dialog.show();
            // 设置点击不可取消
            dialog.setCancelable(false);

            // 获取Window对象
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置Window尺寸
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width=800;
            lp.height= 600;
            window.setAttributes(lp);
            // 设置显示视图内容
            window.setContentView(view_dialog);
        } else {
            dialog.show();
        }
        // 点击'我知道了' 取消dialog
        dialog_tip_iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_registesexbirthday_back: {
                finish();
                break;
            }
            case R.id.tv_birthday: {
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.btn_registesexbirthday_nextstep: {
                String birthday = tv_birthday.getText().toString().trim();
                if (birthday == null || birthday.equals("")) {
                    ToastUtils.show("请输入生日");
                    break;
                }
                Intent intent = new Intent(RegisteSexbirthdayActivity.this, RegisteHeightWeightActivity.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("code", code);
                intent.putExtra("password", password);
                intent.putExtra("session_id", sessionId);
                intent.putExtra("nickname", nickName);
                intent.putExtra("email", email);
                intent.putExtra("birthday", birthday);
                intent.putExtra("sex", selectSex);
                startActivity(intent);
                break;
            }
        }
    }

    /**onCheckedChanged() ，radioButton的监听回调
     * @param group 性别选择的RadioGroup
     * @param checkedId  选择的radioButton 的checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.registe_radio_sex_man:
                selectSex = 1;
                break;
            case R.id.registe_radio_sex_woman:
                selectSex = 2;
                break;
        }
        changeRadiotextStatus();
    }

    private void initPop() {
        View datePickerView = LayoutInflater.from(RegisteSexbirthdayActivity.this).inflate(R.layout.pop_date_picker,null);
        popupWindow = new PopupWindow(datePickerView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);

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
                tv_birthday.setText(year + "-" + (month + 1) + "-" + day);
                popupWindow.dismiss();
            }
        });
    }
}
