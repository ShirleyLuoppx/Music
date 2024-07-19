package com.ppx.music.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ppx.music.NetRequest
import com.ppx.music.R

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：我的-个人中心
 *
 * TODO:暂时先写成activity  后面换成单fragment，多activity的模式
 * TODO:后面可以加一个BaseActivity 可以统一管理activity
 */
class MineActivity : AppCompatActivity(){

    private val netRequest = NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)

        intent.getIntExtra("userId",-1).let { userId ->
            netRequest.getUserDetail(userId)
        }
    }
}