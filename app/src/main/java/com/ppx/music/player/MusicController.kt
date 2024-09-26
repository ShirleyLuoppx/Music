package com.ppx.music.player

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.google.android.exoplayer.C
import com.ppx.music.utils.LogUtils
import java.io.IOException

/**
 *
 * @Author Shirley
 * @Date：2024/9/26
 * @Desc：音乐控制器
 */
@SuppressLint("NotConstructor")
class MusicController() : MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

    private var mediaPlayer: MediaPlayer? = null

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
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.prepareAsync()
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
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }
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

    }

}