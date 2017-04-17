package com.schiwfty.tex.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.schiwfty.tex.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello.text = "hello arran MUCH WOW!!!"
    }
}
