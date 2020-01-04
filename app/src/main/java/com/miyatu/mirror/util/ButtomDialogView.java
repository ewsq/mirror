package com.miyatu.mirror.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.R;

/**
 * Created by yhd on 2019/11/23.
 * Email 1443160259@qq.com
 */
public class ButtomDialogView extends Dialog {
    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private boolean isBackCanCelable;//
    private View view;
    private Context context;

    public ButtomDialogView(Context context, View view, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.view = view;
        this.iscancelable = isCancelable;
        this.isBackCanCelable=isBackCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		//（这里可以R.layout.view）
        setContentView(view);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCanCelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
//    public void showShareTable(){
//        View contentview = LayoutInflater.from(this).inflate(R.layout.popwindow_detail_share, null);
//
//        ButtomDialogView dialogView=new ButtomDialogView(this,contentview,true,true);
//        dialogView.show();
//
//    }
}
