package com.ppx.music.player

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.ppx.music.MusicApplication
import com.ppx.music.common.Constants
import com.ppx.music.http.NetworkService
import com.ppx.music.model.PlayListInfo
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.IOException

/**
 *
 * @Author Shirley
 * @Date：2024/9/26
 * @Desc：音乐控制器
 */
@SuppressLint("NotConstructor")
class MusicController : MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

    private val TAG = "MusicController"
    private var mediaPlayer: MediaPlayer? = null

    //当前播放列表数据
    private var musicDataList: ArrayList<SongDetailInfo> = ArrayList()

    //当前播放的歌曲索引
    private var currentSongIndex = -1

    //当前播放模式：0列表循环、1单曲循环、2随机播放
    private var playMode = 0
    private val networkService = NetworkService.createService()

    companion object {
        val instance: MusicController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicController()
        }
    }

    init {
        mediaPlayer = MediaPlayer()
        // 创建一个AudioAttributes对象，并设置其属性
        val audioAttributes = AudioAttributes.Builder()
        // 设置音频流类型为音乐
        audioAttributes.setUsage(AudioAttributes.USAGE_MEDIA)
        // 设置音频内容类型为音乐
        audioAttributes.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        // 将AudioAttributes对象应用到MediaPlayer
        mediaPlayer?.setAudioAttributes(audioAttributes.build())
//        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC) //deprecated
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnErrorListener(this)
        mediaPlayer?.setOnCompletionListener(this)
    }

    fun playMusic(url: String) {
        try {
            if (!TextUtils.isEmpty(url)) {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(url)
                mediaPlayer?.prepareAsync()
            } else {
                LogUtils.e(TAG, "playMusic : url is empty !!!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun stopMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    //progress 毫秒
    fun seekTo(progress: Int) {
        mediaPlayer?.seekTo(progress)
    }

    fun setPlayMode(mode: Int) {
        playMode = mode
    }

    fun playPreSong(): Int {
        if (playMode == 0 || playMode == 1) {
            if (currentSongIndex > 0) {
                currentSongIndex--
            } else {
                currentSongIndex = musicDataList.size - 1
            }
        } else if (playMode == 2) {
            currentSongIndex = (0 until musicDataList.size).random()
        }
        playSongByIndex(currentSongIndex)
        return currentSongIndex
    }

    fun playNextSong(): Int {
        if (playMode == 0 || playMode == 1) {
            if (currentSongIndex < musicDataList.size - 1) {
                currentSongIndex++
            } else {
                currentSongIndex = 0
            }
        } else if (playMode == 2) {
            //在0到musicDataList.size-1之间取一个随机数
            currentSongIndex = (Math.random() * musicDataList.size).toInt()
        }
        playSongByIndex(currentSongIndex)
        return currentSongIndex
    }

    override fun onPrepared(p0: MediaPlayer?) {
        LogUtils.d(TAG, "onPrepared current song has prepared and ready to start playing!!!")
        p0?.start()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.e(TAG, "Error occurred while playing music: what = $p1 , extra = $p2")
        return false
    }

    override fun onCompletion(p0: MediaPlayer?) {
        LogUtils.d(TAG, "onCompletion current song has finished ....")

        LogUtils.d(
            TAG,
            "onCompletion current song index is = $currentSongIndex and size is = ${musicDataList.size}"
        )

        if (playMode == 0) {
            if (currentSongIndex < musicDataList.size - 1) {
                currentSongIndex++
            } else {
                currentSongIndex = 0
            }
        } else if (playMode == 1) {
            currentSongIndex = currentSongIndex
        } else if (playMode == 2) {
            //在0到musicDataList.size-1之间取一个随机数
            currentSongIndex = (Math.random() * musicDataList.size).toInt()
        }

        LogUtils.d(
            TAG,
            "onCompletion current playMode is = $playMode and index is = $currentSongIndex"
        )

        playSongByIndex(currentSongIndex)
    }

    fun setMusicDataList(musicDataList: ArrayList<SongDetailInfo>) {
        this.musicDataList = musicDataList
    }

    fun getMusicDataList(): ArrayList<SongDetailInfo> {
        return musicDataList
    }

    fun setCurrentSongIndex(index: Int) {
        currentSongIndex = index
    }

    fun getCurrentSongIndex(): Int {
        return currentSongIndex
    }

    private fun playSongByIndex(index: Int) {
        LogUtils.d(TAG, "playSongByIndex current index is = $index")
        val songDetailInfo = musicDataList[index]
        val songId = songDetailInfo.songId

        //使用GlobalScope 会创建一个自定义的协程域，虽然用起来方便，但是可能会导致资源泄漏，因为它不会自动取消，也不会自动回收资源。所以最好还是在activity或者fragment中去使用lifecycleScope，防止泄漏
        GlobalScope.launch(Dispatchers.Main) {
            getSongUrlById(songId)
        }
        EventBus.getDefault().post(songDetailInfo)
    }

    suspend fun getSongUrlById(id: String): String {
        var songUrl = ""
        LogUtils.d(TAG, "getSongUrlById id = $id")

        val newsEntity = networkService.getNewsService(id)

        val jsonObjectStr = JSONObject.parseObject(newsEntity.toString())
        val dataStr = jsonObjectStr["data"].toString()
        val dataArray = JSONObject.parseArray(dataStr)
        if (dataArray.size > 0) {
            val data = dataArray[0].toString()
            val dataObj = JSONObject.parseObject(data)
            val url = dataObj["url"].toString()
            LogUtils.d(TAG, "getSongUrlById url = $url")
            songUrl = url

            startService(url)
        } else {
            LogUtils.d(TAG, "getSongUrlById dataArray.size = 0")
        }
        return songUrl
    }

    suspend fun getDailyRecommendPlayList():  ArrayList<PlayListInfo> {
        val playListStr = networkService.getDailyRecommendPlaylist().toString()
        Log.d(TAG, "getDailyRecommendPlayList: $playListStr")

        return analysisPlayList(playListStr)
    }

    //解析每日推荐歌单数据
    private fun analysisPlayList(playListStr: String): ArrayList<PlayListInfo> {
        LogUtils.d(TAG, "analysisPlayList playListStr = $playListStr")

        val playListInfoList = ArrayList<PlayListInfo>()

        if (playListStr.isNotEmpty()) {
            val jsonObject = JSONObject.parseObject(playListStr)
            val bodyCode = jsonObject["code"]
            if (bodyCode == Constants.CODE_SUCCESS) {
                val recommend = jsonObject["recommend"]
                val dailyRecommendPlayListArray = JSONObject.parseArray(recommend.toString())
                playListInfoList.clear()
                for (playList in dailyRecommendPlayListArray) {
                    val jsonObj = JSONObject.parseObject(playList.toString())
                    val playListId = jsonObj["id"].toString().toFloat()
                    val playListName = jsonObj["name"].toString()
                    val playListPicUrl = jsonObj["picUrl"].toString()
                    val playListPlayCount = jsonObj["playcount"].toString()
                    val playListInfo =
                        PlayListInfo(playListId, playListName, playListPicUrl, playListPlayCount)
                    playListInfoList.add(playListInfo)
                }
                LogUtils.d(
                    TAG,
                    "analysisPlayList success playListInfoList.size = ${playListInfoList.size}"
                )
            } else {
                LogUtils.d(TAG, "analysisPlayList failed code = $bodyCode")
            }
        } else {
            LogUtils.d(TAG, "analysisPlayList playListStr is empty")
        }
        return playListInfoList
    }

    private fun startService(url: String) {
        if (!TextUtils.isEmpty(url)) {
            //使用豆包提供的原生的mediaPlayer也可以正常播放...
            val intent = Intent(MusicApplication.context, MusicPlayerService::class.java)
            intent.putExtra("url", url)
            intent.setAction("PLAY")
            MusicApplication.context.startService(intent)
        }
    }

}