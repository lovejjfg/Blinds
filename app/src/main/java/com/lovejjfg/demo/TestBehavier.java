package com.lovejjfg.demo;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by joe on 2018/7/21.
 * Email: lovejjfg@gmail.com
 */
public class TestBehavier extends CoordinatorLayout.Behavior<LinearLayout> {

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child,
        @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull LinearLayout child,
        @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        System.out.println("dyConsumed::$dyConsumed");
    }
}
