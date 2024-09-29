package com.ppx.music.player

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.text.TextUtils
import com.ppx.music.MusicApplication
import com.ppx.music.NetRequest
import com.ppx.music.model.PlaySongUrlEvent
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    private var mediaPlayer: MediaPlayer? = null
    private var musicDataList: ArrayList<SongDetailInfo> = ArrayList()

    //当前播放的歌曲索引
    private var currentSongIndex = -1
    private var currentSongUrl = ""

    //当前播放模式：0列表循环、1单曲循环、2随机播放
    private var playMode = 0

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
                LogUtils.e("playMusic : url is empty !!!")
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

    fun playPreSong() {
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
    }

    fun playNextSong() {
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
    }

    override fun onPrepared(p0: MediaPlayer?) {
        LogUtils.d("onPrepared current song has prepared and ready to start playing!!!")
        p0?.start()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.e("Error occurred while playing music: what = $p1 , extra = $p2")
        return false
    }

    override fun onCompletion(p0: MediaPlayer?) {
        LogUtils.d("onCompletion current song has finished ....")

        LogUtils.d("onCompletion current song index is = $currentSongIndex and size is = ${musicDataList.size}")

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

        LogUtils.d("onCompletion current playMode is = $playMode and index is = $currentSongIndex")

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
        LogUtils.d("playSongByIndex current index is = $index")
        val songDetailInfo = musicDataList[index]
        val songId = songDetailInfo.songId

        NetRequest.instance.getSongUrlById(songId)
        EventBus.getDefault().post(songDetailInfo)
    }

}