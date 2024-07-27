package com.ppx.music.player

import android.media.AudioManager
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

/**
 *
 * @Author Shirley
 * @Date：2024/7/26
 * @Desc：
 */
class IjkPlayer {
    private var ijkMediaPlayer: IjkMediaPlayer? = null
    private val TAG = "IjkPlayer"

    fun getPlayer(): IjkMediaPlayer? {
        return ijkMediaPlayer
    }

    fun init() {
        LogUtils.d("$TAG   init")
        LogUtils.d("$TAG   ijkMediaPlayer = $ijkMediaPlayer")
        if (ijkMediaPlayer == null) {
            ijkMediaPlayer = IjkMediaPlayer()
            ijkMediaPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 0)
            ijkMediaPlayer!!.setOption(
                IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                "analyzemaxduration",
                50L
            )

            /*  ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"analyzeduration",1);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,"probesize",1024*10);*/
        }
        ijkMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    fun setPathAndPrepare(uri: String?) {
        LogUtils.d("$TAG setPathAndPrepare")
        LogUtils.d("$TAG uri = $uri")
        LogUtils.d("$TAG ijkMediaPlayer = $ijkMediaPlayer")
        try {
            ijkMediaPlayer!!.dataSource = uri
            ijkMediaPlayer!!.stop()
            ijkMediaPlayer!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun start() {
        LogUtils.d("$TAG start")
        LogUtils.d("$TAG ijkMediaPlayer = $ijkMediaPlayer")
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer!!.start()
        }
    }

    fun pause() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer!!.pause()
        }
    }

    fun stop() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer!!.stop()
        }
    }

    /**
     * 耗时
     */
    fun reset() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer!!.reset()
        }
    }

    fun release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer!!.reset()
            ijkMediaPlayer!!.release()
            ijkMediaPlayer = null
        }
    }
}