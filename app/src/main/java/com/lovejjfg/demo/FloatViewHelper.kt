package com.lovejjfg.demo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import com.lovejjfg.blinds.demo.R

/**
 * Created by joe on 2019/2/18.
 * Email: lovejjfg@gmail.com
 */
class FloatViewHelper {
    private var resumeCount = 0
    private var floatView: View? = null
    private val callback: ActivityLifecycleCallback = ActivityLifecycleCallback()

    fun init(application: Application) {
        application.unregisterActivityLifecycleCallbacks(callback)
        application.registerActivityLifecycleCallbacks(callback)
    }

    private fun handleView(activity: Activity?) {
        if (activity == null) {
            return
        }
        if (resumeCount > 0) {
            addFloatView(activity)
        } else {
            hideFloatView(activity)
        }
    }

    private fun hideFloatView(activity: Activity) {
        val floatView = this.floatView ?: return
        floatView.animate()
            .alpha(0f)
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(60)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    activity.windowManager.removeViewImmediate(floatView)
                }
            })
            .start()
        (floatView.layoutParams as? WindowManager.LayoutParams)?.apply {
            activity.putXValue(this.x)
            activity.putYValue(this.y)
        }
        this.floatView = null
    }

    private fun addFloatView(activity: Activity) {
        if (floatView == null) {
            val layoutParams = createLayoutParams(activity)
            val statusBarHeight = activity.getStatusBarHeight()
            layoutParams.gravity = Gravity.TOP or Gravity.START
            val inflate = LayoutInflater.from(activity).inflate(
                R.layout.floating_bt, activity.window.decorView as ViewGroup,
                false
            )
            val view = inflate.findViewById<View>(R.id.fab)
            floatView = inflate
            activity.windowManager.addView(inflate, layoutParams)
            inflate.alpha = 0f
            inflate.scaleX = 0f
            inflate.scaleY = 0f
            inflate.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .start()
            view.setOnTouchListener { v, event ->
                val rawX = (event.rawX - view.width * .5f)
                val rawY = (event.rawY - view.height * .5f)
                layoutParams.x = rawX.toInt()
                layoutParams.y = rawY.toInt() - statusBarHeight
                activity.windowManager.updateViewLayout(inflate, layoutParams)
                return@setOnTouchListener true
            }
        }
    }

    private fun createLayoutParams(activity: Activity): LayoutParams {
        return if (VERSION.SDK_INT >= VERSION_CODES.O) {
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams
                    .TYPE_APPLICATION_OVERLAY,
                LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
        } else {
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams
                    .TYPE_TOAST,
                LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
        }.apply {
            this.x = activity.getXValue()
            this.y = activity.getYValue()
        }
    }

    private fun Context.getStatusBarHeight(): Int {
        // 一般是25dp
        var height = dpToPx(20)
        // 获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    private fun Context.dpToPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

    private fun Context.putXValue(x: Int) {
        val sharedPreferences = getSharedPreferences("Motion", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("x", x).apply()
    }

    private fun Context.putYValue(y: Int) {
        val sharedPreferences = getSharedPreferences("Motion", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("y", y).apply()
    }

    private fun Context.getXValue(): Int {
        val sharedPreferences = getSharedPreferences("Motion", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("x", 0)
    }

    private fun Context.getYValue(): Int {
        val sharedPreferences = getSharedPreferences("Motion", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("y", 0)
    }

    inner class ActivityLifecycleCallback : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
            resumeCount++
            handleView(activity)
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            resumeCount--
            handleView(activity)
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }
}
