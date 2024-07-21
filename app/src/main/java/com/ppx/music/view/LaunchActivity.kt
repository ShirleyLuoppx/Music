package com.ppx.music.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.databinding.ActivityLaunchBinding
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：首页-flash页面
 */
class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private var netUtils = NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = ActivityLaunchBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)
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