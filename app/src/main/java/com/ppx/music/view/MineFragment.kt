package com.ppx.music.view

import android.net.Uri
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
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

/**
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：我的
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
        val userId = 494817816
        getUserDetail(userId)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
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

                    analysisUserInfo(body)
                }
            }
        })
    }

    private fun analysisUserInfo(info: String) {
        val json = JSON.parseObject(info)

        //等级
        val level = json["level"].toString()
        binding.tvLevel.text = "Lv.${level}等级"
        LogUtils.d("getUserDetailsInfo onSuccess level = $level")

        val profileObj = json.getJSONObject("profile")

        //头像
        val avatarUrl = profileObj["avatarUrl"].toString()
        Glide.with(requireActivity()).load(avatarUrl).into(binding.ivAvatarUrl)

        //昵称
        val nickname = profileObj["nickname"].toString()
        LogUtils.d("getUserDetailsInfo onSuccess nickname = $nickname")
        binding.tvNickname.text = nickname

        //vip状态
        val vipType = profileObj["vipType"].toString()
        if (vipType == "0") {
            binding.cvVipInfo.visibility = View.VISIBLE
            binding.cvVip.visibility = View.GONE
        } else {
            binding.cvVipInfo.visibility = View.GONE
            binding.cvVip.visibility = View.VISIBLE
        }

        val signature = profileObj["signature"].toString()
        binding.tvSignature.text = signature

        //TODO:城市

        val gender = profileObj["gender"].toString()
        LogUtils.d("getUserDetailsInfo onSuccess gender = $gender")
        val genderPic = if (gender == "1") {
            resources.getDrawable(R.mipmap.ic_famale)
        } else {
            resources.getDrawable(R.mipmap.ic_male)
        }
        binding.ivGender.setImageDrawable(genderPic)


        //TODO:几零后
        //TODO:星座
        //TODO:村龄

        val follows = profileObj["follows"].toString()
        binding.tvFollows.text = "$follows 关注"

        val fans = profileObj["followeds"].toString()
        binding.tvFolloweds.text = "$fans 粉丝"



        //TODO：听歌时长


    }

}