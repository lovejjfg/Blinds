package com.lovejjfg.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.lovejjfg.blinds.demo.R
import kotlinx.android.synthetic.main.activity_main.blinds

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..20) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds.addView(imageView)
        }
    }
}
