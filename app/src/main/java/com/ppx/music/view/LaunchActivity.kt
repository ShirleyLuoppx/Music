package com.ppx.music.view

import android.content.Intent
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.databinding.ActivityLaunchBinding
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：首页-flash页面
 */
class LaunchActivity :BaseActivity<ActivityLaunchBinding>() {

    private var netUtils = NetRequest()

    override fun initView() {
    }

    override fun initListener() {
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

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_launch
    }

}