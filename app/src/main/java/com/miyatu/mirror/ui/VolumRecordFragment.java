package com.miyatu.mirror.ui;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.activity.EditVolumeRecordActivity;
import com.miyatu.mirror.adapter.FriendVolumeAdapter;
import com.miyatu.mirror.widget.CommonIndicator;

/**
 * Created by yhd on 2019/11/19.
 * Email 1443160259@qq.com
 */
public class VolumRecordFragment extends PublicFragment {
    private TextView tvEdit;//编辑按钮
    private CommonIndicator commonIndicator;        //自定义滑动布局
    private ViewPager viewPager;;//滑动viewpager
    private MyVolumeRecordFragment myVolumeRecordFragment;
    private FriendVolumeRecordFragment friendVolumeRecordFragment;
    private FriendVolumeAdapter friendVolumeAdapter;
    private boolean isEdit = false;
    private boolean isEdit2 = false;
    private MyAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_volume;
    }

    @Override
    protected void initView(View view) {
        tvEdit = view.findViewById(R.id.tv_edit);
        commonIndicator = view.findViewById(R.id.commonIndicator);
        viewPager = view.findViewById(R.id.view_pager);
        initFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditVolumeRecordActivity.startActivity(getActivity());
            }
        });
    }

    @Override
    protected void updateView() {

    }

    public void initFragment(){
        myVolumeRecordFragment = new MyVolumeRecordFragment();
        friendVolumeRecordFragment = new FriendVolumeRecordFragment();
        adapter = new MyAdapter(getChildFragmentManager(),myVolumeRecordFragment,friendVolumeRecordFragment);
        viewPager.setAdapter(adapter);
        commonIndicator.bindViewPager(viewPager);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

        public MyAdapter(FragmentManager fm, MyVolumeRecordFragment myVolumeRecordFragment, FriendVolumeRecordFragment friendVolumeRecordFragment) {
            super(fm);
            fragmentSparseArray.put(0, myVolumeRecordFragment);
            fragmentSparseArray.put(1, friendVolumeRecordFragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentSparseArray.get(position);
        }

        @Override
        public int getCount() {
            return fragmentSparseArray.size();
        }

    }

    public void editFinish(){

    }
}
