package com.lovejjfg.demo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import com.lovejjfg.blinds.demo.R

/**
 * Created by joe on 2019/2/18.
 * Email: lovejjfg@gmail.com
 */
class App : Application() {
    private var resumeCount = 0
    private var floatView: View? = null
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
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
        })
    }

    private fun handleView(activity: Activity?) {
        if (activity == null) {
            return
        }
        if (resumeCount > 0) {
            addTopView(activity)
        } else {
            hideView(activity)
        }
    }

    private fun hideView(activity: Activity) {
        if (floatView != null) {
            try {
                activity.windowManager.removeViewImmediate(floatView)
            } catch (e: Exception) {
                Log.e("error:", "hideError:", e)
            }
            floatView = null
        }
    }

    private fun addTopView(activity: Activity) {
        if (floatView == null) {
            val layoutParams = createLayoutParams()
            val statusBarHeight = getStatusBarHeight()
            layoutParams.gravity = Gravity.TOP or Gravity.START
            val inflate = LayoutInflater.from(activity).inflate(
                R.layout.floating_bt, activity.window.decorView as ViewGroup,
                false
            )
            val view = inflate.findViewById<View>(R.id.fab)
            floatView = inflate
            activity.windowManager.addView(inflate, layoutParams)
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

    private fun createLayoutParams(): LayoutParams {
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
        }
    }

    fun Context.getStatusBarHeight(): Int {
        // 一般是25dp
        var height = dpToPx(20)
        // 获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    fun Context.dpToPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
}
