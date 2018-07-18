package com.lovejjfg.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.lovejjfg.blinds.demo.R
import kotlinx.android.synthetic.main.activity_main.blinds
import kotlinx.android.synthetic.main.activity_main.blinds1
import kotlinx.android.synthetic.main.activity_main.blinds2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        blinds.setOnClickListener {
            blinds.toggle()
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds1.addView(imageView)
        }
        for (i in 0..6) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.mipmap.ic_launcher)
            blinds2.addView(imageView)
        }
    }
}
