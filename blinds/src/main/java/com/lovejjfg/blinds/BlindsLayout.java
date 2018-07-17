package com.lovejjfg.blinds;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by joe on 2018/7/16.
 * Email: lovejjfg@gmail.com
 */
public class BlindsLayout extends ViewGroup {

    private int mTotalLength;
    private int maxCount = 3;
    private int blindsSize = 60;
    private ValueAnimator layoutChangeAnimator;

    public BlindsLayout(Context context) {
        this(context, null);
    }

    public BlindsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    private boolean expand = true;
    private int extraSpace = 0;
    private float animatedFraction = 1.0f;

    public BlindsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);

        layoutChangeAnimator = ValueAnimator
            .ofFloat(0f, 1.0f)
            .setDuration(300);
        layoutChangeAnimator.setInterpolator(new FastOutLinearInInterpolator());
        layoutChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedFraction = animation.getAnimatedFraction();
                requestLayout();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand = !expand;
                layoutChangeAnimator.cancel();
                layoutChangeAnimator.start();
            }
        });
    }

    @Override
    public final void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= maxCount) {
            return;
        }
        super.addView(child, index, params);
        if (getChildCount() == maxCount) {
            expand = false;
            layoutChangeAnimator.start();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        performClick();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return childCount - 1 - i;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        System.out.println("on measure..");
        int widthSize = getPaddingLeft() + getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int maxWidth = 0;
        if (widthMode == MeasureSpec.AT_MOST) {
            maxWidth = 0;
        }
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            maxWidth = 0;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int heightSize = getPaddingBottom() + getPaddingBottom();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            final LayoutParams lp = child.getLayoutParams();
            int heightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            int widthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            child.measure(widthSpec, heightSpec);
            int height = child.getMeasuredHeight();
            heightSize = Math.max(heightSize, height);
            widthSize += child.getMeasuredWidth();
        }
        widthSize += extraSpace * (childCount - 1);
        heightSize += getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(Math.max(widthSize, maxWidth), heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren(l, t, r, b);
    }

    void layoutChildren(int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();//getPaddingLeftWithForeground();
        final int parentRight = right - left - getPaddingRight(); //- getPaddingRightWithForeground();

        final int parentTop = getPaddingTop();// getPaddingTopWithForeground();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int dy = 0;
                if (i < count - 1) {
                    if (!expand) {
                        //todo 未展开情况
                        dy = (int) ((count - i - 1) * blindsSize * animatedFraction);
                    } else {
                        for (int j = i + 1; j <= count - 1; j++) {
                            int measuredWidth = getChildAt(j).getMeasuredWidth();
                            dy += measuredWidth;
                        }
                        dy *= animatedFraction;
                    }
                    dy += (count - i - 1) * extraSpace;
                }
                child.layout(parentRight - width - dy, parentTop, parentRight - dy, parentTop + height);
            }
        }
    }
}
