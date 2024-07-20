package com.ppx.music.view

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.ActivityLoginBinding
import com.ppx.music.databinding.ActivityMineBinding
import com.ppx.music.utils.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：我的-个人中心
 *
 * TODO:暂时先写成activity  后面换成单fragment，多activity的模式
 * TODO:后面可以加一个BaseActivity 可以统一管理activity
 * TODO:子线程网络请求
 */
class MineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMineBinding
    private val netRequest = NetRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mine);

        val userId = 494817816
        getUserDetail(userId)



    }

    /**
     * 获取用户详情
     * /user/detail?uid=494817816
     */
    private fun getUserDetail(uid: Int) {
        val request: Request = Request.Builder()
            .url(ApiConstants.GET_USER_DETAIL + "?uid=" + uid)
            .get()
            .build()

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtils.d("getUserDetail onFailure: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("getUserDetailsInfo onResponse")

                runOnUiThread {
                    val body = response.body!!.string()
                    LogUtils.d("getUserDetail onResponse: $body")

                    val json = JSON.parseObject(body)

                    val level = json["level"].toString()
                    val tvLevel:TextView =binding.tvLevel
                    tvLevel.text = level
                    findViewById<TextView>(R.id.tv_level).text = level
                    LogUtils.d("getUserDetailsInfo onSuccess level = $level")

                    val profileObj = json.getJSONObject("profile")

                    val gender = profileObj["gender"].toString()
                    LogUtils.d("getUserDetailsInfo onSuccess gender = $gender")

                    val tvUserId :TextView = binding.tvUserId
                    tvUserId.text = profileObj["userId"].toString()
                    LogUtils.d("getUserDetailsInfo onSuccess tvUserId = $tvUserId")

                    val sex = if (gender == "1") {
                        "男"
                    } else {
                        "女"
                    }
                    val tvGender: TextView = binding.tvGender
                    tvGender.text = sex
                    val nickname = profileObj["nickname"].toString()
                    LogUtils.d("getUserDetailsInfo onSuccess nickname = $nickname")
                    binding.tvNickname.text = nickname

                    val avatarUrl = profileObj["avatarUrl"].toString()
                    //                binding.ivAvatarUrl.setImageURI(Uri.parse(avatarUrl))

                    val vipType = profileObj["vipType"].toString()
                    val isVip = if (vipType == "0") {
                        "请开通会员"
                    } else {
                        "尊贵的vip您好"
                    }
                    binding.tvVipType.text = isVip

                    val signature = profileObj["signature"].toString()
                    binding.tvSignature.text = signature

                    val fans = profileObj["followeds"].toString()
                    binding.tvFolloweds.text = fans

                    val follows = profileObj["follows"].toString()
                    binding.tvFollows.text = follows
                }


            }
        })


    }

}