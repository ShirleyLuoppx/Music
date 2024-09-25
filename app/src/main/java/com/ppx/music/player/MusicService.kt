package com.ppx.music.player

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.Binder
import android.os.IBinder
import com.ppx.music.player.MediaService.Companion.PLAY_URI
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 *
 * @Author Shirley
 * @Date：2024/9/25
 * @Desc：
 */
class MusicService : Service(), OnPreparedListener, OnErrorListener, OnCompletionListener {

    private lateinit var player: IjkMediaPlayer
    private val binder: IBinder = MusicPlayerBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = IjkMediaPlayer()
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnPreparedListener(this)
        player.setOnErrorListener(this)
        player.setOnCompletionListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(PLAY_URI)
        play(url)
        LogUtils.d("1111111111111111111  MusicService   111111111111111111111115 ")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    override fun onPrepared(iMediaPlayer: IMediaPlayer?) {
        iMediaPlayer?.start()
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.e("onError p1 = $p1  p2 = $p2")
        return false
    }

    override fun onCompletion(p0: IMediaPlayer?) {

    }

    fun play(url: String?) {
        player.reset()
        player.setDataSource(url)
        player.prepareAsync()
    }

    fun stop() {
        if (player.isPlaying) {
            player.stop()
            player.reset()
        }
    }

    class MusicPlayerBinder : Binder() {
        val service: MusicService
            get() = MusicService()
    }
}