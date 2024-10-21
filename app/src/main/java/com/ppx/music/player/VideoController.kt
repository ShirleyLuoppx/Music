package com.ppx.music.player

import android.net.Uri
import android.view.SurfaceHolder
import com.ppx.music.MusicApplication
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 *
 * @Author Shirley
 * @Date：2024/10/19
 * @Desc：视频播放控制器
 */
class VideoController : IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
    IMediaPlayer.OnErrorListener {

    private val TAG = "VideoController"
    private val ijkMediaPlayer = IjkMediaPlayer()

    companion object {
        val instance: VideoController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            VideoController()
        }
    }

    init {
        ijkMediaPlayer.setOnPreparedListener(this)
        ijkMediaPlayer.setOnCompletionListener(this)
        ijkMediaPlayer.setOnErrorListener(this)
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onPrepared: ")
        start()
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onCompletion: ")
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.d(TAG, "onError: p1: $p1, p2: $p2")
        return false
    }

    fun start() {
        ijkMediaPlayer.start()
    }

    fun pauseOrStart() {
        if (ijkMediaPlayer.isPlaying) {
            ijkMediaPlayer.pause()
        } else {
            ijkMediaPlayer.start()
        }
    }

    fun stop() {
        if (ijkMediaPlayer.isPlaying) {
            ijkMediaPlayer.stop()
        }
    }

    fun release() {
        if (ijkMediaPlayer.isPlaying) {
            ijkMediaPlayer.stop()
        }
        ijkMediaPlayer.reset()
        ijkMediaPlayer.release()
    }

    fun seekTo(seekTo: Long) {
        ijkMediaPlayer.seekTo(seekTo)
    }

    fun isPlaying(): Boolean {
        return ijkMediaPlayer.isPlaying
    }

    fun setDataSource(p0: SurfaceHolder, url: String) {
        ijkMediaPlayer.reset()
        ijkMediaPlayer.setDisplay(p0)
        ijkMediaPlayer.setDataSource(MusicApplication.context, Uri.parse(url))
        ijkMediaPlayer.prepareAsync()
    }
}