package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.adapter.ViewPagerAdapter;
import com.miyatu.mirror.fragment.EditFriendVolumeRecordFragment;
import com.miyatu.mirror.fragment.EditMyVolumeRecordFragment;

import java.util.ArrayList;

public class EditVolumeRecordActivity extends PublicActivity {
    private ImageView ivBack;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private String[] titles = new String[]{"我的量体记录","亲友量体记录"};

    public static void startActivity(Context context){
        Intent intent = new Intent(context, EditVolumeRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_volume;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        initFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void updateView() {

    }

    public void initFragment(){
        fragments = new ArrayList<>();
        fragments.add(new EditMyVolumeRecordFragment());
        fragments.add(new EditFriendVolumeRecordFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setOffscreenPageLimit(2);         //设置缓存页面数
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
