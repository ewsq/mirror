package com.miyatu.mirror.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miyatu.mirror.R;
import com.tozmart.tozisdk.api.OnEditListener;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.utils.BitmapHolder;
import com.tozmart.tozisdk.view.EditOutlineView;
import com.tozmart.tozisdk.view.editprofileview.CurrentMode;

import static com.tozmart.tozisdk.constant.PhotoType.FRONT;

public class EditOutlineActivity extends AppCompatActivity {
    EditOutlineView editOutlineView;
    FloatingActionButton undoBtn;
    FloatingActionButton doneBtn;

    private int flag;

    private Profile2ModelData profile2ModelData;
    private boolean hasEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_out_line);

        editOutlineView = findViewById(R.id.photo_drawee_view);
        undoBtn = findViewById(R.id.undo_btn);
        doneBtn = findViewById(R.id.done_btn);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            flag = bundle.getInt("whichSide", 0);
            profile2ModelData = bundle.getParcelable("profile2ModelData");
        }
        if (profile2ModelData == null) {
            return;
        }

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editOutlineView != null) {
                    editOutlineView.undo();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 再次保存当前修改过的点集
                profile2ModelData = editOutlineView.getSavedProfile2ModelData();

                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("profile2ModelData", profile2ModelData);
                bundle1.putBoolean("hasEdit", hasEdit);
                Intent intent = new Intent();
                intent.putExtras(bundle1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setUpPhotoView();
    }

    private void setUpPhotoView() {
        if (flag == FRONT){
            editOutlineView.initialize(
                    flag,
                    BitmapHolder.getFrontBitmap(),
                    profile2ModelData,
                    true
            );
        } else {
            editOutlineView.initialize(
                    flag,
                    BitmapHolder.getSideBitmap(),
                    profile2ModelData,
                    true
            );
        }
        editOutlineView.setOutlineGoodColor(ContextCompat.getColor(this, R.color.outline_color));
        editOutlineView.setOutlineBadColor(ContextCompat.getColor(this, R.color.outline_error_color));
        editOutlineView.setMovePanColor(ContextCompat.getColor(this, R.color.colorAccent));
        editOutlineView.setMoveRectSrc(ContextCompat.getDrawable(this, R.drawable.ic_image_gray_24dp));
        editOutlineView.setOnEditListener(new OnEditListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onEdit(CurrentMode currentMode, boolean undoBtnVisible) {
                hasEdit = true;
                if (currentMode == CurrentMode.NONE) {
                    undoBtn.setVisibility(undoBtnVisible ? View.VISIBLE : View.GONE);
                } else {
                    undoBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
