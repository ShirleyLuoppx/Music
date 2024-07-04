package com.ppx.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.common.ApiConstants
import com.ppx.music.utils.LogUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var netUtils= NetRequest()
    private var phoneNumber :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnGetVerifyCode.setOnClickListener {
            LogUtils.d("click btnGetVerifyCode btn.......")

            val internationalCode = binding.tvInternationalDialingCode.text.toString()
            phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("onCreate phoneNumber = $phoneNumber")

            netUtils.sendVerifyCode(ApiConstants.sendVerifyCode,"phone",phoneNumber)
        }

        binding.btnLogin.setOnClickListener {
            val verifyCode =  binding.etVerifyCode.text.toString()
            LogUtils.d("click btnLogin verifyCode = $verifyCode")
            netUtils.checkVerifyCode(phoneNumber,verifyCode)
        }

        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun logout(){
        netUtils.logout()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("onDestroy")
    }
}