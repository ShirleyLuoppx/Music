package com.ppx.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.ppx.music.utils.ApiConstants
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.Login
import com.ppx.music.utils.NetUtils

class MainActivity : AppCompatActivity() {

    lateinit var login:Login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_verify_code).setOnClickListener {

            LogUtils.d("click btn.......")

            val et:EditText = findViewById(R.id.et_phone_number)
            login = Login()
            login.sendVerifyCodeByPhone(et.text.toString())

        }

    }
}