package com.ppx.music.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.FragmentPlayerBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.player.MediaService
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.TimeTransUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.text.StringBuilder

class PlayerActivity : BaseActivity<FragmentPlayerBinding>() {

    private var clickSongDetailInfo: SongDetailInfo? = SongDetailInfo()

    override fun initView() {

    }

    override fun initListener() {
    }

    @SuppressLint("NewApi")
    override fun initData() {
//        val clickSongId = intent.getStringExtra("clickSongId")
//        LogUtils.d("initData clickSongId = $clickSongId")

        clickSongDetailInfo =
            intent.getParcelableExtra("clickSongDetailInfo")
        LogUtils.d("initData clickSongDetailInfo = $clickSongDetailInfo")

        if (clickSongDetailInfo != null) {
            val clickSongId = clickSongDetailInfo?.songId
            if (clickSongId != null) {
                getSongUrlById(clickSongId)
            }

            initPlayerData(clickSongDetailInfo!!)
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_player
    }

    private fun getSongUrlById(id: String) {
        val requestBody = FormBody.Builder()
            .add("id", id)
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(ApiConstants.GET_URL_BY_SONG_ID)
            .post(requestBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LogUtils.d("getSongUrlById onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                LogUtils.d("getSongUrlById onResponse body = $bodyStr")

                val jsonObjectStr = JSONObject.parseObject(bodyStr)
                val dataStr = jsonObjectStr["data"].toString()
                val dataArray = JSONObject.parseArray(dataStr)
                if (dataArray.size > 0) {
                    val data = dataArray[0].toString()
                    val dataObj = JSONObject.parseObject(data)
                    val url = dataObj["url"].toString()
                    LogUtils.d("getSongUrlById url = $url")


//                    MediaService.startPlay(this@PlayerActivity, url)
                } else {
                    LogUtils.d("getSongUrlById dataArray.size = 0")
                }
            }

        })

    }

    private fun initPlayerData(songData: SongDetailInfo) {
//        binding.cvMusicRotateImg.background =
        Glide.with(this).load(songData.picUrl).into(binding.spiMusicRotate)
        binding.tvSongName.text = songData.songName


        val singerSb = StringBuilder()
        songData.songArtists.forEach {
            singerSb.append(it)
            singerSb.append("/")
        }
        binding.tvSingerName.text = singerSb.dropLast(1)


        val timeStr = TimeTransUtils.long2Minutes(songData.songTime)
        binding.tvTotalTime.text = timeStr

    }
}