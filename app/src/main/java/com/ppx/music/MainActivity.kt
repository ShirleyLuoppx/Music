package com.ppx.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.common.ApiConstants
import com.ppx.music.utils.LogUtils

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var netUtils= NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerifyCode.setOnClickListener {
            LogUtils.d("click btn.......")

            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click sendVerifyCode and phoneNumber = $phoneNumber")
            netUtils.sendVerifyCode(ApiConstants.sendVerifyCode,"phone",phoneNumber)
        }

        val etVerifyCode = findViewById<EditText>(R.id.et_verify_code)


        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click btnLogin and phoneNumber = $phoneNumber")
            val verifyCode = etVerifyCode.text.toString()
            LogUtils.d("click btnLogin and verifyCode = $verifyCode")

            netUtils.checkVerifyCode(ApiConstants.checkVerifyCode,phoneNumber,verifyCode)
        }

        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun logout(){
        netUtils.logout()
    }
}