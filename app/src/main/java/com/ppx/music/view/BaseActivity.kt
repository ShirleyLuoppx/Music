package com.ppx.music.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {

    interface initView
    interface initData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}