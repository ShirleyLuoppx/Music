package com.ppx.music.view.recommend

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendSongAdapter
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.databinding.FragmentDailyRecommendBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.model.SongVipStatus
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.view.BaseFragment
import com.ppx.music.view.MusicPlayerActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlin.random.Random

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：每日推荐歌曲列表
 */
class DailyRecommendSongFragment : BaseFragment<FragmentDailyRecommendBinding>() {

    private val songsInfoList = ArrayList<SongDetailInfo>()
    val dailyRecommendAdapter = DailyRecommendSongAdapter()

    override fun initView() {

        initRv()
    }

    override fun initListener() {

        dailyRecommendAdapter.setOnItemClickListener { adapter, view, position ->

            MusicController.instance.setCurrentSongIndex(position)

            val clickSongDetailInfo = adapter.getItem(position)
            val clickSongId = clickSongDetailInfo?.songId
            val clickSongName = clickSongDetailInfo?.songName
            LogUtils.d("onItemClick $position and clickSongId = $clickSongId and clickSongName = $clickSongName")

            val intent = Intent(requireActivity(), MusicPlayerActivity::class.java)
            intent.putExtra("clickSongDetailInfo", clickSongDetailInfo)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            fragmentManager?.popBackStack()
            transaction?.commit()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        getDailyRecommendSongs()

        //获取当前的月/日
        val currentDate = java.util.Calendar.getInstance()
        val month = currentDate.get(java.util.Calendar.MONTH) + 1
        val day = currentDate.get(java.util.Calendar.DAY_OF_MONTH)

        val sbString = SpannableString("$day / $month")
        sbString.setSpan(
            AbsoluteSizeSpan(50, true),
            0,
            month.toString().length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
        sbString.setSpan(
            AbsoluteSizeSpan(13, true),
            month.toString().length,
            day.toString().length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.tvDate.text = sbString
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_daily_recommend
    }

    override fun onDestroyFragment() {
    }


    private val okHttpClient = OkHttpClient()
    private fun getDailyRecommendSongs() {
        val request = Request.Builder()
            .url(ApiConstants.GET_DAILY_RECOMMEND_SONGS)
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("onFailure: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body!!.string()
                LogUtils.d("onResponse : $bodyStr")

                if (response.code == Constants.CODE_SUCCESS) {

                    val data = analysisData(bodyStr)
                    if (data.size > 0) {
                        activity?.runOnUiThread {
                            dailyRecommendAdapter.addAll(data)

                            Glide.with(this@DailyRecommendSongFragment)
                                .load(data[Random.nextInt(0, data.size)].picUrl)
                                .into(binding.ivTopBg)
                        }
                    }
                }
            }
        })
    }

    private fun analysisData(bodyStr: String): ArrayList<SongDetailInfo> {
        val bodyJSONObject = JSONObject.parseObject(bodyStr)
        val bodyCode = bodyJSONObject["code"]
        if (bodyCode == Constants.CODE_SUCCESS) {

            val data = bodyJSONObject["data"]
            val dailySongsJO = JSONObject.parseObject(data.toString())
            LogUtils.d("dailySongsJO = $dailySongsJO")

            val dailySongsStr = dailySongsJO["dailySongs"]
            LogUtils.d("onResponse : dailySongsArray = $dailySongsStr")

            val dailySongsArray = JSONObject.parseArray(dailySongsStr.toString())
            LogUtils.d("onResponse : dailySongsArray = ${dailySongsArray.size}")

            for (dailySong in dailySongsArray) {


                val dailySongData = dailySong.toString()
                val jsonObject = JSONObject.parseObject(dailySongData)
                //歌名
                val songName = jsonObject["name"].toString()
//                LogUtils.d("onResponse : songName = $songName")

                //歌曲id
                val songId = jsonObject["id"].toString()
//                LogUtils.d("onResponse : songId = $songId")

                //歌手名
                val ar = jsonObject["ar"].toString()
                val arArray = JSONObject.parseArray(ar)
                val singersList = ArrayList<String>()
                for (arInfo in arArray) {
                    val singleArStr = JSONObject.parseObject(arInfo.toString())
                    val singerName = singleArStr["name"].toString()
                    LogUtils.d("onResponse : when songName = $songName , singerName = $singerName")
                    singersList.add(singerName)
                }

                //歌曲/专辑图片
                val alStr = jsonObject["al"].toString()
                val alObj = JSONObject.parseObject(alStr)
                val picUrl = alObj["picUrl"].toString()
                val alName = alObj["name"].toString()
                //是否是会员歌曲
                val fee = jsonObject["fee"].toString()
                val songVipStatus = initSongVipStatus(fee)
                //歌曲时间 毫秒
                val songTime = jsonObject["dt"].toString().toLong()

                //点赞数量
                //讨论数量

                val songDetailInfo = SongDetailInfo(
                    songId,
                    songName,
                    singersList,
                    alName,
                    picUrl,
                    songVipStatus,
                    songTime
                )
                songsInfoList.add(songDetailInfo)
            }

            MusicController.instance.setMusicDataList(songsInfoList)

        }
        return songsInfoList
    }

    private fun initRv() {
        LogUtils.d("initRv songsInfoList size = ${songsInfoList.size}")
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvDailyRecommend.layoutManager = layoutManager
        binding.rvDailyRecommend.adapter = dailyRecommendAdapter
    }

    /**
     * feeStr:
     * 0:免费或无版权
     * 1:VIP 歌曲
     * 4:购买专辑
     * 8:非会员可免费播放低音质，会员可播放高音质及下载
     * fee为1或8的歌曲均可单独购买 2元单曲
     */
    private fun initSongVipStatus(feeStr: String): SongVipStatus {
        var vipStatus = SongVipStatus.FREE
        when (feeStr) {
            "0" -> {
                vipStatus = SongVipStatus.FREE
            }

            "1" -> {
                vipStatus = SongVipStatus.VIP
            }

            "4" -> {
                vipStatus = SongVipStatus.BUY_ALBUM
            }

            "8" -> {
                vipStatus = SongVipStatus.ALL_CAN_PLAY
            }
        }
        return vipStatus
    }


}