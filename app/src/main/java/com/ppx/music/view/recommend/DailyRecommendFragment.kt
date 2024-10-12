package com.ppx.music.view.recommend

import android.content.Intent
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendAdapter
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.databinding.FragmentDailyRecommendBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.model.SongVipStatus
import com.ppx.music.mvvm.DailyRecommendViewModel
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.view.BaseFragment
import com.ppx.music.view.PlayerActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：每日推荐
 */
class DailyRecommendFragment : BaseFragment<FragmentDailyRecommendBinding>() {

//    private val dailyRecommendViewModel by viewModel<DailyRecommendViewModel>()
    private val dailyRecommendViewModel  = DailyRecommendViewModel()
    private val songsInfoList = ArrayList<SongDetailInfo>()
    val dailyRecommendAdapter = DailyRecommendAdapter()

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

            //跳转到播放页面
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.framelayout,this)
//            transaction?.commit()

            val intent = Intent(requireActivity(), PlayerActivity::class.java)
            intent.putExtra("clickSongDetailInfo",clickSongDetailInfo)
            startActivity(intent)
        }
    }

    override fun initData() {
        getDailyRecommendSongs()
//       val data  = dailyRecommendViewModel.getDailyRecommendSongs()
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
//                            dailyRecommendAdapter.notifyDataSetChanged()
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
//                Log.d(TAG, "analysisData: ")
                val songVipStatus = initSongVipStatus(fee)
                //歌曲时间 毫秒
                val songTime = jsonObject["dt"].toString().toLong()

                //点赞数量
                //讨论数量

                val songDetailInfo = SongDetailInfo(songId, songName, singersList, alName, picUrl,songVipStatus,songTime)
                songsInfoList.add(songDetailInfo)


//                dailyRecommendAdapter.notifyDataSetChanged()
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

//        dailyRecommendAdapter.addAll(songsInfoList)
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