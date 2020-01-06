package com.miyatu.mirror.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.R;
import com.miyatu.mirror.activity.AccountBind;
import com.miyatu.mirror.activity.LoginActivity;
import com.miyatu.mirror.activity.PersonInfo;
import com.miyatu.mirror.bean.UserDatabean;
import com.miyatu.mirror.util.ScreenUtils;
import com.miyatu.mirror.util.ShareUtils;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yhd on 2019/11/21.
 * Email 1443160259@qq.com
 */
public class MineFragment extends PublicFragment implements View.OnClickListener {
    private ImageView ivInformation;
    private ImageView ivHead;
    private TextView tvMineName;
    private LinearLayout llMineAdmin;
    private LinearLayout llMineSound;
    private LinearLayout llMineShare;
    private LinearLayout llMineQuestion;
    private LinearLayout llMineEdition;
    private ImageView ivShareWeixin;
    private ImageView ivShareWeixinFriendCircle;
    private ImageView ivShareQQ;
    private ImageView ivClose;
    private LinearLayout ll;
    private LinearLayout linUserData;
    private Button logout;

    private String userJson;

    private PopupWindow popVolume;
    private PopupWindow popShare;

    private MediaPlayer mediaPlayer;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

        llMineAdmin = view.findViewById(R.id.ll_mine_admin);
        llMineEdition = view.findViewById(R.id.ll_mine_edition);
        llMineQuestion = view.findViewById(R.id.ll_mine_question);
        llMineShare = view.findViewById(R.id.ll_mine_share);
        llMineSound = view.findViewById(R.id.ll_mine_sound);
        ivHead = view.findViewById(R.id.iv_head);
        tvMineName = view.findViewById(R.id.tv_mine_name);
        ivInformation = view.findViewById(R.id.iv_information);
        linUserData = view.findViewById(R.id.linUserData);
        logout = view.findViewById(R.id.logout);

        initPopVolume();
        initPopShare();
    }

    @Override
    protected void initData() {
        initUserData();

        System.out.println("####step token=" + getUserDataBean().getToken());
    }

    @Override
    protected void initEvent() {
        llMineSound.setOnClickListener(this);
        llMineAdmin.setOnClickListener(this);
        llMineShare.setOnClickListener(this);
        llMineQuestion.setOnClickListener(this);
        llMineEdition.setOnClickListener(this);
        llMineQuestion.setOnClickListener(this);
        ivInformation.setOnClickListener(this);
        linUserData.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            default:
                break;
            case R.id.linUserData:
                //编辑自己资料
                userJson = PreferencesUtils.getString(getActivity(), PreferencesUtils.USERDATA, "");
                if (userJson == null || userJson.equals("")) {
                    LoginActivity.startActivity(getActivity());
                }
                else {
                    PersonInfo.startActivity(getActivity());
                }
                break;
            case R.id.ll_mine_share:        //分享魔镜
                ScreenUtils.dimBackground(getActivity(),1f,0.5f);       //屏幕亮度变化
                popShare.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_mine_sound:            //自拍音量
                ScreenUtils.dimBackground(getActivity(),1f,0.5f);       //屏幕亮度变化
                popVolume.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
            case R.id.ll_mine_question:
                //问题反馈
                FeedbackActivity.startActivity(getActivity());
                break;
            case R.id.ll_mine_edition:
                //版本
                EditionActivity.startActivity(getActivity());
                break;
            case R.id.ll_mine_admin:
                //账户管理绑定
                AccountBind.startActivity(getActivity());
                break;
            case R.id.iv_information:
                InformationActivity.startActivity(getActivity());
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferencesUtils.clearPreferences(getActivity());
                        initUserData();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setMessage("确定退出用户吗？")
                .create();
        alertDialog.show();
    }

    public void initPopShare() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_detail_share, null);
        popShare = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ivClose = view.findViewById(R.id.iv_close);
        ivShareWeixin = view.findViewById(R.id.iv_share_weixin);
        ivShareWeixinFriendCircle = view.findViewById(R.id.iv_share_weixin_frient_circle);
        ivShareQQ = view.findViewById(R.id.iv_share_qq);
        ll = view.findViewById(R.id.ll);

        popShare.setOutsideTouchable(true);
        popShare.setClippingEnabled(false);
        popShare.setFocusable(true);

        ivShareWeixinFriendCircle.setOnClickListener(new View.OnClickListener() {           //分享到微信朋友圈
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_edition, null);
                ShareUtils.shareToWXFriendCircle(getActivity(), "http://mojing.meyatu.com/index/index/share", "魔镜", "魔镜", bitmap);
            }
        });
        ivShareWeixin.setOnClickListener(new View.OnClickListener() {               //分享到微信
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_edition, null);
                ShareUtils.shareToWX(getActivity(), "http://mojing.meyatu.com/index/index/share", "魔镜", "魔镜", bitmap);
            }
        });
        ivShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popShare.dismiss();
            }
        });

        popShare.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(getActivity(),0.5f,1f);       //屏幕亮度变化
            }
        });

    }

    private void initPopVolume() {
        final ImageView ivSoundOpen;
        final ImageView ivSoundClose;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_popip, null, false);
        popVolume = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);

        LinearLayout llOpen =  view.findViewById(R.id.ll_open);
        LinearLayout llClose = view.findViewById(R.id.ll_close);
        ivSoundOpen =  view.findViewById(R.id.iv_sound_open);
        ivSoundClose = view.findViewById(R.id.iv_sound_close);

        popVolume.setOutsideTouchable(true);
        popVolume.setClippingEnabled(false);
        popVolume.setFocusable(true);

        //设置popupWindow里的按钮的事件
        llOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //点击开启
                ivSoundOpen.setVisibility(View.VISIBLE);
                ivSoundClose.setVisibility(View.INVISIBLE);
                popVolume.dismiss();
            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //点击关闭
                ivSoundOpen.setVisibility(View.INVISIBLE);
                ivSoundClose.setVisibility(View.VISIBLE);
                popVolume.dismiss();
            }
        });

        popVolume.setOnDismissListener(new PopupWindow.OnDismissListener() {          //popupWindow消失时
            @Override
            public void onDismiss() {
                ScreenUtils.dimBackground(getActivity(),0.5f,1f);       //屏幕亮度变化
            }
        });
    }

    private void initUserData() {
        userJson = PreferencesUtils.getString(getActivity(), PreferencesUtils.USERDATA, "");
        if (userJson == null || userJson.equals("")) {
            tvMineName.setText("未登录，点击登录");
        }
        else {
            tvMineName.setText(getUserDataBean().getNickname());
        }
        Glide.with(getActivity()).load(MyApp.imageUrl + getUserDataBean().getHead_pic()).placeholder( R.mipmap.edit_person_info_headicon).into(ivHead);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserDatabean.DataBean dataBean) {
        initUserData();
    }

}
