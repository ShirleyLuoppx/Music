package com.ppx.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.common.ApiConstants
import com.ppx.music.utils.LogUtils

class MainActivity : AppCompatActivity() {

//    lateinit var login:Login
    lateinit var binding: ActivityMainBinding
    var netUtils= NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        findViewById<Button>(R.id.btn_verify_code).setOnClickListener {
            LogUtils.d("click btn.......")

            val internationalCode = binding.tvInternationalDialingCode.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("onCreate phoneNumber = $phoneNumber")

//            login = Login()
//            login.sendVerifyCodeByPhone(et.text.toString())

//            netUtils.postString(ApiConstants.loginUrlByPhone,phoneNumber)
            netUtils.postLoginForms(ApiConstants.loginUrl,"phone",phoneNumber)
        }

        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun logout(){
        netUtils.logout()
    }
}