package com.ppx.music.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppx.music.NetRequest
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：首页-flash页面
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var netUtils = NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvJumpLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.tvGetLoginStatus.setOnClickListener {
            val loginStatus = netUtils.getLoginStatus()
            LogUtils.d("loginStatus = $loginStatus")

            if (loginStatus) {
                binding.tvGetLoginStatus.text = "已登录"
            } else {
                binding.tvGetLoginStatus.text = "未登录"
            }
        }
    }
}