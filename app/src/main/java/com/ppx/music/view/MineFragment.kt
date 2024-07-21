package com.ppx.music.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.FragmentMineBinding
import com.ppx.music.utils.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MineFragment : Fragment() {

    private lateinit var binding: FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData() {
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

                requireActivity().runOnUiThread {
                    val body = response.body!!.string()
                    LogUtils.d("getUserDetail onResponse: $body")

                    val json = JSON.parseObject(body)

                    val level = json["level"].toString()
                    val tvLevel: TextView = binding.tvLevel
                    tvLevel.text = level
                    binding.tvLevel.text = level
                    LogUtils.d("getUserDetailsInfo onSuccess level = $level")

                    val profileObj = json.getJSONObject("profile")

                    val gender = profileObj["gender"].toString()
                    LogUtils.d("getUserDetailsInfo onSuccess gender = $gender")

                    val tvUserId: TextView = binding.tvUserId
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