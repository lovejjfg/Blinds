package com.lovejjfg.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup.LayoutParams
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import com.lovejjfg.blinds.BlindsLayout.RIGHT
import com.lovejjfg.blinds.demo.R
import kotlinx.android.synthetic.main.activity_main.blind3
import kotlinx.android.synthetic.main.activity_main.blinds
import kotlinx.android.synthetic.main.activity_main.blinds1
import kotlinx.android.synthetic.main.activity_main.blinds2
import kotlinx.android.synthetic.main.activity_main.blinds4
import kotlinx.android.synthetic.main.activity_main.blinds5

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        blinds.setOnClickListener {
            blinds.toggle()
            blind3.toggle()
//            if (blinds4.orientation == LEFT) {
//                blinds4.orientation = RIGHT
//            } else {
//                blinds4.orientation = LEFT
//            }
            if (blinds4.fraction == 0.6F) {
                blinds4.fraction = 1F
                blinds4.isRevertDraw = true
            } else {
                blinds4.fraction = 0.6F
                blinds4.isRevertDraw = false
            }
            println("${blinds4.fraction}")
        }
        blinds4.orientation = RIGHT
        blinds4.maxCount = 4
        blinds4.extraSpace = 20
        blinds4.isRevertDraw = true
        blinds4.setAnimationDuration(1000)
        blinds4.setInterpolator(BounceInterpolator())
        blinds5.orientation = RIGHT
        blind3.orientation = RIGHT
        for (i in 0..2) {
            val imageView = ImageView(this)
            imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds1.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds2.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blind3.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds4.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds5.addView(imageView)
        }
    }
}
