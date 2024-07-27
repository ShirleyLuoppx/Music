package com.ppx.music.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ppx.music.R

/**
 *
 * @Author Shirley
 * @Date：2024/7/23
 * @Desc：
 */
class TestMainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.test_frame_layout,TestDrawerLayoutFragment())
            .commit()
    }
}