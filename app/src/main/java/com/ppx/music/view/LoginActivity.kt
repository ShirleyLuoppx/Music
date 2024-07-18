package com.ppx.music.view

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.ActivityLoginBinding
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：登录界面
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var netUtils = NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerifyCode.setOnClickListener {
            LogUtils.d("click btn.......")

            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click sendVerifyCode and phoneNumber = $phoneNumber")
            netUtils.checkPhone(phoneNumber)
//            netUtils.sendVerifyCode(ApiConstants.sendVerifyCode,"phone",phoneNumber)
        }

        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click btnLogin and phoneNumber = $phoneNumber")
            val verifyCode =  binding.etVerifyCode.text.toString()
            LogUtils.d("click btnLogin and verifyCode = $verifyCode")

            netUtils.checkVerifyCode(phoneNumber,verifyCode)
        }

        binding.btnLogout.setOnClickListener { logout() }

        binding.tvGetUserid.setOnClickListener {
            netUtils.getUserIdByNickName("oldsportox")
        }
    }

    private fun logout(){
        netUtils.logout()
    }
}