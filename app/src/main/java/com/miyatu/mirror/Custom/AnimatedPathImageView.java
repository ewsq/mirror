package com.miyatu.mirror.Custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.tozmart.tozisdk.entity.PaintColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt
 */
public class AnimatedPathImageView extends AppCompatImageView {

    public static final int DRAW_DURATION = 2000;

    private float mPathLeftLength;
    private float mPathRightLength;
    private float mPathErrorLength;
    private Paint mPaint;
    private Path mPathLeft;
    private Path mPathRight;
    private Path mPathError;

    public AnimatedPathImageView(Context context) {
        this(context, null);
    }

    public AnimatedPathImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedPathImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), com.tozmart.tozisdk.R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 让画的线圆滑
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * Set the drawn path using an array of array of floats. First is x parameter, second is y.
     *
     * @param points The points to set on
     */
    public void setPath(ArrayList<PointF> points, List<PaintColor> pointLoose) {
        if (points == null || points.size() == 0) {
            return;
        }

        mPathLeft = new Path();
        mPathRight = new Path();
        mPathError = new Path();
        points.add(points.get(0));
//        points.add(0, points.get(points.size() - 1));
        List<PointF> pointsLeft = new ArrayList<>();
        List<PointF> pointsRight = new ArrayList<>();
        int l = points.size();
        for (int i = l / 2; i >= 0; i--) {
            pointsLeft.add(points.get(i));
        }
        for (int i = l / 2; i < l; i++) {
            pointsRight.add(points.get(i));
        }

        mPathLeft.moveTo(pointsLeft.get(0).x, pointsLeft.get(0).y);
        for (PointF point : pointsLeft) {
            mPathLeft.lineTo(point.x, point.y);
        }
        PathMeasure mPathMeasureLeft = new PathMeasure(mPathLeft, false);
        mPathLeftLength = mPathMeasureLeft.getLength();

        mPathRight.moveTo(pointsRight.get(0).x, pointsRight.get(0).y);
        for (PointF point : pointsRight) {
            mPathRight.lineTo(point.x, point.y);
        }
        PathMeasure mPathMeasureRight = new PathMeasure(mPathRight, false);
        mPathRightLength = mPathMeasureRight.getLength();

        if (mPathLeftLength > mPathRightLength) {
            startAnimLeft((long) (DRAW_DURATION / mPathRightLength * mPathLeftLength));
            startAnimRight(DRAW_DURATION);
        } else {
            startAnimLeft(DRAW_DURATION);
            startAnimRight((long) (DRAW_DURATION / mPathLeftLength * mPathRightLength));
        }

        if (pointLoose != null) {
            for (PaintColor paintColor : pointLoose) {
                mPathError.moveTo(points.get(paintColor.getLoosePartStart()).x, points.get(paintColor.getLoosePartStart()).y);
                for (int i = paintColor.getLoosePartStart(); i < paintColor.getLoosePartEnd(); i++) {
                    mPathError.lineTo(points.get(i).x, points.get(i).y);
                }
            }
            PathMeasure mPathMeasureError = new PathMeasure(mPathError, false);
            mPathErrorLength = mPathMeasureError.getLength();
            startAnimError(DRAW_DURATION / 5);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPathLeft != null && mPathRight != null) {
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            canvas.drawPath(mPathLeft, mPaint);
            canvas.drawPath(mPathRight, mPaint);
        }
        if (mPathError != null) {
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            canvas.drawPath(mPathError, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(widthMeasureSpec);

        int measuredWidth, measuredHeight;

        if (widthMode == View.MeasureSpec.AT_MOST)
            throw new IllegalStateException("AnimatedPathView cannot have a WRAP_CONTENT property");
        else
            measuredWidth = widthSize;

        if (heightMode == View.MeasureSpec.AT_MOST)
            throw new IllegalStateException("AnimatedPathView cannot have a WRAP_CONTENT property");
        else
            measuredHeight = heightSize;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    // 开启动画
    public void startAnimLeft(long duration) {
        ValueAnimator valueAnimatorLeft = ValueAnimator.ofFloat(0.f, 1.f);
        valueAnimatorLeft.setDuration(duration);
        // 减速插值器
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                // 获取当前点坐标封装到currentDrawPoint
                PathEffect pathEffect = new DashPathEffect(new float[]{
                        mPathLeftLength, mPathLeftLength},
                        (mPathLeftLength - mPathLeftLength * value));
                mPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });
        valueAnimatorLeft.start();
    }

    public void startAnimRight(long duration) {
        ValueAnimator valueAnimatorRight = ValueAnimator.ofFloat(0.f, 1.f);
        valueAnimatorRight.setDuration(duration);
        // 减速插值器
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimatorRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                // 获取当前点坐标封装到currentDrawPoint
                PathEffect pathEffect = new DashPathEffect(new float[]{
                        mPathRightLength, mPathRightLength},
                        (mPathRightLength - mPathRightLength * value));
                mPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });
        valueAnimatorRight.start();
    }

    public void startAnimError(long duration) {
        ValueAnimator valueAnimatorRight = ValueAnimator.ofFloat(0.f, 1.f);
        valueAnimatorRight.setDuration(duration);
        // 减速插值器
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimatorRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                // 获取当前点坐标封装到currentDrawPoint
                PathEffect pathEffect = new DashPathEffect(new float[]{
                        mPathErrorLength, mPathErrorLength},
                        (mPathErrorLength - mPathErrorLength * value));
                mPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });
        valueAnimatorRight.start();
    }

    public Bitmap getBitmap() {
        if (getDrawable() instanceof BitmapDrawable) {
            return ((BitmapDrawable) getDrawable()).getBitmap();
        }
        return null;
    }
}
