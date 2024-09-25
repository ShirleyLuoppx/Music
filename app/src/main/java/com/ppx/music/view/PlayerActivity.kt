package com.ppx.music.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.ppx.music.MusicApplication
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.databinding.FragmentPlayerBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.player.DBMusicPlayer
import com.ppx.music.player.MediaService
import com.ppx.music.player.MediaService.Companion.PLAY_URI
import com.ppx.music.player.MusicPlayerService
import com.ppx.music.player.MusicService
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.TimeTransUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import tv.danmaku.ijk.media.player.IjkMediaPlayer
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


//                    MusicService.startPlay(MusicApplication.context, url)

//在service里面去播放就会容易崩溃
                    val intent = Intent(MusicApplication.context, DBMusicPlayer::class.java)
                    intent.putExtra("url", url)
                    intent.setAction("PLAY")
                    startService(intent)

                    //在service里面去播放就会容易崩溃
//                    val intent = Intent(MusicApplication.context, MusicService::class.java)
//                    intent.putExtra("PLAY_URI", url) //.setAction(PLAY)
//                    startService(intent)

                    //使用豆包提供的原生的mediaPlayer也可以正常播放...
//                    val intent = Intent(MusicApplication.context, MusicPlayerService::class.java)
//                    intent.putExtra("url", url)
//                    intent.setAction("PLAY")
//                    startService(intent)

                    //直接用这种的就基本能播放
//                    val player = IjkMediaPlayer()
//                    player.setDataSource(MusicApplication.context, Uri.parse(url))
//                    player.prepareAsync()
//                    player.start()

                    /**
                     * Crashes：
                     * Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x430a0000 in tid 4915 (ff_read), pid 4787 (com.ppx.music)
                     * Fatal signal 7 (SIGBUS), code 1 (BUS_ADRALN), fault addr 0x73736500000001 in tid 10718 (ff_read), pid 10627 (com.ppx.music)
                     * Using Network Security Config from resource network_security_config debugBuild: true
                     */

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