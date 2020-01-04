package com.miyatu.mirror.activity;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.fragment.TakePhotoFragment;
import com.miyatu.mirror.ui.MineFragment;
import com.miyatu.mirror.ui.VolumRecordFragment;

/**
 * 首页activity
 */
public class HomeActivity extends PublicActivity implements View.OnClickListener {
    private LinearLayout ll_record_tabitem=null;//记录 tabItem
    private LinearLayout ll_takephoto_tabitem=null;//拍照 tabItem
    private LinearLayout ll_my_tabitem=null;//我的 tabItem

    private LinearLayout tab_select_record=null;//记录选中的样式
    private LinearLayout tab_unselect_record=null;//记录未选中的样式

    private LinearLayout tab_select_takephoto=null;//拍照选中的样式
    private LinearLayout tab_unselect_takephoto=null;//拍照未选中的样式

    private LinearLayout tab_select_my=null;//我的选中的样式
    private LinearLayout tab_unselect_my=null;//我的未选中的样式

    private FragmentManager mFragmentManager=null;
    private FragmentTransaction fragmentTransaction=null;

    private VolumRecordFragment mVolumRecordFragment=null;//记录Fragment
    private TakePhotoFragment mTakePhotoFragment=null;//拍照Fragment
    private MineFragment mMyFragment=null;//我的Fragment

    private boolean mIsExit;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    protected void initView() {
        ll_record_tabitem=findViewById(R.id.ll_record_tabitem);
        ll_record_tabitem.setOnClickListener(this);
        ll_takephoto_tabitem=findViewById(R.id.ll_takephoto_tabitem);
        ll_takephoto_tabitem.setOnClickListener(this);
        ll_my_tabitem=findViewById(R.id.ll_my_tabitem);
        ll_my_tabitem.setOnClickListener(this);

        tab_select_record=findViewById(R.id.tab_select_record);
        tab_unselect_record=findViewById(R.id.tab_unselect_record);

        tab_select_takephoto=findViewById(R.id.tab_select_takephoto);
        tab_unselect_takephoto=findViewById(R.id.tab_unselect_takephoto);

        tab_select_my=findViewById(R.id.tab_select_my);
        tab_unselect_my=findViewById(R.id.tab_unselect_my);

        mFragmentManager = getSupportFragmentManager();
        initFragment();
        setDefaultsetectFragment();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

    private void initFragment() {

        mVolumRecordFragment=new VolumRecordFragment();
        mTakePhotoFragment=new TakePhotoFragment();
        mMyFragment=new MineFragment();

        //‘ll_homefragment_content’是添加Fragment的容器，添加每个Fragment
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ll_homefragment_content,mVolumRecordFragment);
        fragmentTransaction.add(R.id.ll_homefragment_content,mTakePhotoFragment);
        fragmentTransaction.add(R.id.ll_homefragment_content,mMyFragment);
        fragmentTransaction.commit();

    }
    /**
     * 设置默认显示的记录fragment，还有底部导航栏的默认显示状态（记录选中，其他的未选择状态）
     */
    private void setDefaultsetectFragment() {
        setTabItemUnselect();
        tab_select_record.setVisibility(View.VISIBLE);
        tab_unselect_record.setVisibility(View.GONE);

        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.show(mVolumRecordFragment).hide(mTakePhotoFragment).hide(mMyFragment).commit();

    }

    @Override
    public void onClick(View v) {
        fragmentTransaction = mFragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.ll_record_tabitem:{//点击‘记录’ tabItem
                setTabItemUnselect();//重置所有菜单为‘未选中的显示状态’
                tab_select_record.setVisibility(View.VISIBLE);//显示‘记录’选中的状态
                tab_unselect_record.setVisibility(View.GONE);//隐藏‘记录’没选中的状态

                fragmentTransaction
                        .show(mVolumRecordFragment)//显示 记录Fragment
                        .hide(mTakePhotoFragment)//隐藏 拍照fragment
                        .hide(mMyFragment)//隐藏 我的fragment
                        .commit();
                break;
            }
            case R.id.ll_takephoto_tabitem:{
                setTabItemUnselect();
                tab_select_takephoto.setVisibility(View.VISIBLE);
                tab_unselect_takephoto.setVisibility(View.GONE);

                fragmentTransaction.hide(mVolumRecordFragment).show(mTakePhotoFragment).hide(mMyFragment).commit();
                break;
            }
            case R.id.ll_my_tabitem:{
                setTabItemUnselect();
                tab_select_my.setVisibility(View.VISIBLE);
                tab_unselect_my.setVisibility(View.GONE);
                fragmentTransaction.hide(mVolumRecordFragment).hide(mTakePhotoFragment).show(mMyFragment).commit();
                break;
            }
        }

    }

    /**
     * 把所以的底部菜单栏都设置成未选择的显示状态，为了在切换的时候，重置状态的作用
     */
    private void setTabItemUnselect(){
        tab_select_record.setVisibility(View.GONE);
        tab_unselect_record.setVisibility(View.VISIBLE);
        tab_select_takephoto.setVisibility(View.GONE);
        tab_unselect_takephoto.setVisibility(View.VISIBLE);
        tab_select_my.setVisibility(View.GONE);
        tab_unselect_my.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audioManager  = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsExit == false) {
                    mIsExit = true;
                    ToastUtils.show("再按一次退出");
                    handler.sendEmptyMessageDelayed(0, 1500);
                    return true;
                } else {
                    finish();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
        }
        return true;
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIsExit = false;
        }
    };

}
