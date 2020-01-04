package com.miyatu.mirror.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.miyatu.mirror.util.DisplayUtil;


/**
 * CommonIndicator 用于我的资料中每个列表项的布局
 * 具有下面自定义的属性
 * app:indiText01="
 * app:indiText02=""
 * app:indiWidth=""
 * app:indiIndex = ""
 */
public class CommonIndicator extends LinearLayout {
    private ViewPager viewPager;
    public TextView text01Tv;
    public TextView text02Tv;
    private View index01;
    private View index02;

    private String text01 ="";
    private String text02 ="";
    private int index = 0;
    private int width;

    public CommonIndicator(Context context) {
        super(context, null);
    }

    public CommonIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView(context);

    }

    private void initAttrs(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonIndicator);
            text01 = a.getString(R.styleable.CommonIndicator_indiText01);
            text02 = a.getString(R.styleable.CommonIndicator_indiText02);
            width = a.getDimensionPixelSize(R.styleable.CommonIndicator_indiWidth, 32);
            index = a.getInteger(R.styleable.CommonIndicator_indiIndex, 0);
            a.recycle();
        } else {
            width = DisplayUtil.dp2px(getContext(), 32);
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R
                .layout.widget_common_indicator, this);
        text01Tv = findViewById(R.id.tv_text01);
        text02Tv = findViewById(R.id.tv_text02);
        index01 = findViewById(R.id.iv_index01);
        index02 = findViewById(R.id.iv_index02);

        text01Tv.setText(text01);
        text02Tv.setText(text02);
        setTextBold(index);

        index01.setMinimumWidth(width);
        index02.setMinimumWidth(width);

        findViewById(R.id.ll_indicator_01).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager != null){
                    viewPager.setCurrentItem(0);
                }
            }
        });
        findViewById(R.id.ll_indicator_02).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager != null){
                    viewPager.setCurrentItem(1);
                }
            }
        });
    }

    public void bindViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setTextBold(i);
                setIndicatorVisible(i);
                setTextColor(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    /**
     * 设置粗体
     */
    public void setTextBold(int  index) {
        text01Tv.getPaint().setFakeBoldText(index ==0 ? true: false);
        text02Tv.getPaint().setFakeBoldText(index !=0 ? true: false);
    }
    public void setIndicatorVisible(int index){
        index01.setVisibility(index ==0 ? VISIBLE: INVISIBLE);
        index02.setVisibility(index !=0 ? VISIBLE: INVISIBLE);
    }

    public void  setTextColor(int index)
    {
        text01Tv.setTextColor(index == 0? Color.parseColor("#7F9EA9") : Color.BLACK);
        text02Tv.setTextColor(index != 0? Color.parseColor("#7F9EA9") : Color.BLACK);

    }

}
