package com.miyatu.mirror.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.miyatu.mirror.R;


public class NewVolumeDate extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    private EditText et_new_volume_username=null,//姓名
            et_new_volume_height=null,//身高
            et_new_volume_weight=null;//体重
    private ImageView iv_new_volume_data_back=null;//返回键
    private TextView new_volume__radiotext_sex_woman=null,new_volume__radiotext_sex_man=null;//‘男’‘女’textview

    private RadioGroup new_volume__radiogroup_sex=null;//性别选择radiogroup，添加check监听

    private Button btn_new_volume_data_save=null;//保存 按钮

    private String newVolumeDataSelectSex="男";//性别选择的值，默认‘男’

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_volume_date);
        initView();
    }

    private void initView() {
        et_new_volume_username=findViewById(R.id.et_new_volume_username);
        et_new_volume_height=findViewById(R.id.et_new_volume_height);
        et_new_volume_weight=findViewById(R.id.et_new_volume_weight);

        iv_new_volume_data_back=findViewById(R.id.iv_new_volume_data_back);
        iv_new_volume_data_back.setOnClickListener(this);
        new_volume__radiotext_sex_woman=findViewById(R.id.new_volume_radiotext_sex_woman);
        new_volume__radiotext_sex_man=findViewById(R.id.new_volume_radiotext_sex_man);

        new_volume__radiogroup_sex=findViewById(R.id.new_volume_radiogroup_sex);
        new_volume__radiogroup_sex.setOnCheckedChangeListener(this);


        btn_new_volume_data_save=findViewById(R.id.btn_new_volume_data_save);
        btn_new_volume_data_save.setOnClickListener(this);
        changeRadiotextStatus();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_new_volume_data_back: {
                finish();
//                Toast.makeText(NewVolumeDate.this, "返回按键"
//                        , Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.dialog_tip_iknow: {
//                Toast.makeText(NewVolumeDate.this, "我知道了"
//                        , Toast.LENGTH_SHORT).show();
                break;
            }
//            case R.id.et_birthday: {
//                Toast.makeText(NewVolumeDate.this, "请输入生日"
//                        , Toast.LENGTH_SHORT).show();
//                break;
//            }
            case R.id.btn_new_volume_data_save: {
//                Toast.makeText(NewVolumeDate.this, "下一步" + "姓名："
//                                + et_new_volume_username.getText()  + "性别："
//                                + newVolumeDataSelectSex + "身高："
//                                + et_new_volume_height.getText()+ "体重："
//                                + et_new_volume_weight.getText()
//                        , Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * 根据选择的radiobutton改变radiobutton_text的颜色
     */
    private void changeRadiotextStatus() {
        if (newVolumeDataSelectSex.equals("男")){
            new_volume__radiotext_sex_man.setText("男");
            new_volume__radiotext_sex_woman.setHint("");
            new_volume__radiotext_sex_woman.setText("");
            new_volume__radiotext_sex_woman.setHint("女");
        }else if (newVolumeDataSelectSex.equals("女")){
            new_volume__radiotext_sex_man.setText("");
            new_volume__radiotext_sex_man.setHint("男");
            new_volume__radiotext_sex_woman.setText("女");
            new_volume__radiotext_sex_woman.setHint("");
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton=findViewById(checkedId);
        newVolumeDataSelectSex=radioButton.getTag().toString();
        changeRadiotextStatus();

        //radioButton.getTag()   ‘男’or‘女’；
//        Toast.makeText(this, "选择了"+radioButton.getTag(), Toast.LENGTH_SHORT).show();
    }
}
