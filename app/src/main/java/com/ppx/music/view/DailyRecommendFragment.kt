package com.ppx.music.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendAdapter
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.databinding.FragmentDailyRecommendBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：每日推荐
 */
class DailyRecommendFragment : Fragment() {

    private lateinit var binding: FragmentDailyRecommendBinding
    private val songsInfoList = ArrayList<SongDetailInfo>()
    val dailyRecommendAdapter = DailyRecommendAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_recommend, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDailyRecommendSongs()
        initRv()

    }

    private fun getDailyRecommendSongs() {
        val okHttpClient = OkHttpClient()
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
                            dailyRecommendAdapter.notifyDataSetChanged()
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
                LogUtils.d("onResponse : songName = $songName")

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

                val songDetailInfo = SongDetailInfo(-1, songName, singersList, alName, picUrl)
                songsInfoList.add(songDetailInfo)


//                dailyRecommendAdapter.notifyDataSetChanged()
            }
        }
//        initRv()

        return songsInfoList
    }

    private fun initRv() {
        LogUtils.d("initRv songsInfoList size = ${songsInfoList.size}")
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        dailyRecommendAdapter.addAll(songsInfoList)
        binding.rvDailyRecommend.layoutManager = layoutManager
        binding.rvDailyRecommend.adapter = dailyRecommendAdapter

        dailyRecommendAdapter.setOnItemClickListener { adapter, view, position ->
            LogUtils.d("onItemClick $position")
        }
    }

    private fun initListener() {

    }
}