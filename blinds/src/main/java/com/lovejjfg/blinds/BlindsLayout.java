package com.lovejjfg.blinds;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by joe on 2018/7/16.
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings("unused")
public class BlindsLayout extends ViewGroup {

    @IntDef(flag = true, value = {
        LEFT,
        RIGHT
    })
    @Target({ PARAMETER, METHOD })
    @Retention(RetentionPolicy.SOURCE)
    @interface Orientation {
    }

    private static final FastOutLinearInInterpolator INTERPOLATOR = new FastOutLinearInInterpolator();
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private int maxCount = 3;
    private float fraction = 0.8F;
    private ValueAnimator layoutChangeAnimator;
    private boolean fold = true;
    private int extraSpace = 0;
    private float animatedFraction = 1.0f;
    private static final int ANIMATION_DURATION = 300;
    private int orientation = LEFT;
    private boolean revertDraw = true;

    public BlindsLayout(Context context) {
        this(context, null);
    }

    public BlindsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BlindsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BlindsLayout);
        fraction = a.getFraction(R.styleable.BlindsLayout_blindsFraction, 1, 1, fraction);
        extraSpace = a.getDimensionPixelSize(R.styleable.BlindsLayout_blindsExtraSpace, extraSpace);
        maxCount = a.getInt(R.styleable.BlindsLayout_blindsMaxCount, maxCount);
        fold = a.getBoolean(R.styleable.BlindsLayout_blindsFold, fold);
        orientation = a.getInt(R.styleable.BlindsLayout_blindsFoldOrientation, LEFT);
        revertDraw = a.getBoolean(R.styleable.BlindsLayout_blindsRevertDraw, revertDraw);
        a.recycle();
        setChildrenDrawingOrderEnabled(revertDraw);
        layoutChangeAnimator = ValueAnimator
            .ofFloat(0f, 1.0f)
            .setDuration(ANIMATION_DURATION);
        layoutChangeAnimator.setInterpolator(INTERPOLATOR);
        layoutChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedFraction = animation.getAnimatedFraction();
                requestLayout();
            }
        });
    }

    public void toggle() {
        fold = !fold;
        layoutChangeAnimator.cancel();
        layoutChangeAnimator.start();
    }

    public void setMaxCount(int maxCount) {
        if (this.maxCount != maxCount) {
            this.maxCount = maxCount;
            requestLayout();
        }
    }

    /**
     * fold fraction 1.0 means fold 100% 0 means not fold.
     */
    public void setFraction(float fraction) {
        if (fraction < 0 || fraction > 1F) {
            throw new IllegalArgumentException("fraction must in [0,1] ,but you set :" + fraction);
        }
        if (this.fraction != fraction) {
            this.fraction = fraction;
            requestLayout();
        }
    }

    public void setFold(boolean fold) {
        if (this.fold != fold) {
            this.fold = fold;
            requestLayout();
        }
    }

    public void setExtraSpace(int extraSpace) {
        if (this.extraSpace != extraSpace) {
            this.extraSpace = extraSpace;
            requestLayout();
        }
    }

    public void setAnimationDuration(long duration) {
        if (layoutChangeAnimator != null) {
            layoutChangeAnimator.setDuration(duration);
        }
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        if (layoutChangeAnimator != null) {
            layoutChangeAnimator.setInterpolator(interpolator);
        }
    }

    public void setOrientation(@Orientation int orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            requestLayout();
        }
    }

    public boolean isRevertDraw() {
        return revertDraw;
    }

    public void setRevertDraw(boolean revertDraw) {
        if (this.revertDraw != revertDraw) {
            this.revertDraw = revertDraw;
            setChildrenDrawingOrderEnabled(revertDraw);
            requestLayout();
        }
    }

    public int getMaxCount() {
        return maxCount;
    }

    public float getFraction() {
        return fraction;
    }

    public boolean isFold() {
        return fold;
    }

    public int getExtraSpace() {
        return extraSpace;
    }

    public @Orientation
    int getOrientation() {
        return orientation;
    }

    @Override
    public final void addView(View child, int index, LayoutParams params) {
        if (getChildCount() >= maxCount) {
            return;
        }
        if (child.getVisibility() != VISIBLE) {
            return;
        }
        super.addView(child, index, params);
        if (getChildCount() == maxCount) {
            layoutChangeAnimator.start();
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return childCount - 1 - i;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getPaddingLeft() + getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int maxWidth = 0;
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            maxWidth = 0;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int heightSize = getPaddingBottom() + getPaddingBottom();
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
        if (orientation == RIGHT) {
            layoutChildrenRight(l, r);
        } else {
            layoutChildrenLeft(l, r);
        }
    }

    void layoutChildrenRight(int left, int right) {
        final int count = getChildCount();
        final int parentRight = right - left - getPaddingRight();
        int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int offset = 0;
                if (i < count - 1) {
                    if (fold) {
                        offset = (int) ((count - i - 1) * (1 - fraction) * width * animatedFraction);
                    } else {
                        for (int j = i + 1; j <= count - 1; j++) {
                            int measuredWidth = getChildAt(j).getMeasuredWidth();
                            offset += measuredWidth;
                        }
                        offset *= animatedFraction;
                    }
                    if (extraSpace < 0 && -i * extraSpace > offset) {
                        offset = 0;
                    } else {
                        offset += (count - i - 1) * extraSpace;
                    }
                }
                child.layout(parentRight - width - offset, parentTop, parentRight - offset, parentTop + height);
            }
        }
    }

    void layoutChildrenLeft(int left, int right) {
        final int count = getChildCount();
        final int parentRight = getPaddingLeft();
        int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int offset = 0;
                if (i > 0) {
                    if (fold) {
                        offset = (int) (i * (1 - fraction) * width * animatedFraction);
                    } else {
                        for (int j = 0; j <= i - 1; j++) {
                            int measuredWidth = getChildAt(j).getMeasuredWidth();
                            offset += measuredWidth;
                        }
                        offset *= animatedFraction;
                    }
                    if (extraSpace < 0 && -i * extraSpace > offset) {
                        offset = 0;
                    } else {
                        offset += i * extraSpace;
                    }
                }
                child.layout(parentLeft + offset, parentTop, parentLeft + offset + width, parentTop + height);
            }
        }
    }
}
