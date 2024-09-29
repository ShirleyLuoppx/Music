package com.ppx.music.view.mine

import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.FragmentMineBinding
import com.ppx.music.model.MessageEvent
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.ProvincesUtils
import com.ppx.music.view.BaseFragment
import com.ppx.music.viewmodel.UserDetailVM
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException

/**
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：我的
 */
class MineFragment : BaseFragment<FragmentMineBinding>() {

    private lateinit var viewmodel: UserDetailVM

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
        EventBus.getDefault().register(this)
//        viewmodel = ViewModelProvider(this)[UserDetailVM::class.java]
//        binding.viewModel = viewmodel
//        binding.lifecycleOwner = this

        val userId = 494817816
        getUserDetail(userId)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun onDestroyFragment() {
        EventBus.getDefault().unregister(this)
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

                val body = response.body!!.string()
                //暂时先这样用eventbus把数据传出去更新，后续改成mvvm
                EventBus.getDefault().post(MessageEvent(body))
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:MessageEvent ){
        LogUtils.d("bodyString = ${event.body}")
        if(!TextUtils.isEmpty(event.body)){
            analysisUserInfo(event.body!!)
        }
    }

    private fun analysisUserInfo(info: String) {
        val json = JSON.parseObject(info)

        //等级
        val level = json["level"].toString()
        binding.tvLevel.text = "Lv.${level}等级"
        LogUtils.d("getUserDetailsInfo onSuccess level = $level")

        val listenSongs = json["listenSongs"].toString()
        binding.tvListenSongHours.text = "${listenSongs}首"

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

        //个性签名
        val signature = profileObj["signature"].toString()
        binding.tvSignature.text = signature

        //城市
        val provinceId = profileObj["province"].toString().toInt()
        val cityName = ProvincesUtils.getProvinceStrById(provinceId)
        LogUtils.d("getUserDetailsInfo onSuccess cityName = $cityName")
        binding.tvCity.text = cityName

        //性别
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

        //关注
        val follows = profileObj["follows"].toString()
        binding.tvFollows.text = "$follows 关注"

        //粉丝
        val fans = profileObj["followeds"].toString()
        binding.tvFolloweds.text = "$fans 粉丝"


        //TODO：听歌时长


    }


}