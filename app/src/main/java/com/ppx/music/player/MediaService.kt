package com.ppx.music.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener

class MediaService : Service() ,OnErrorListener,OnPreparedListener {

    //ijkplayer
    private lateinit var ijkAudioPlayer: IjkAudioPlayer



    companion object {
        val PLAY_URI = "PLAY_URI"
        val PLAY = "PLAY"

        fun startPlay(context: Context, uri: String) {
            context.startService(
                Intent(context, MediaService::class.java).putExtra(PLAY_URI, uri).setAction(PLAY)
            )
        }

    }

//    public static void startPlay(Context context, String uri) {
//        context.startService(new Intent(context, MediaService.class).putExtra(PLAY_URI, uri).setAction(PLAY));
//    }
//
//    //继续播放、若已发生暂停
//    public static void continuePlay(Context context) {
//        context.startService(new Intent(context, MediaService.class).setAction(CONTINUE_PLAY));
//    }
//
//
//    public static void pausePlay(Context context) {
//        context.startService(new Intent(context, MediaService.class).setAction(PAUSE));
//    }
//
//    public static void stopPlay(Context context) {
//        context.startService(new Intent(context, MediaService.class).setAction(STOP));
//    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

//        if (ijkAudioPlayer == null) {
            ijkAudioPlayer = IjkAudioPlayer()
            ijkAudioPlayer.init()
            ijkAudioPlayer.player.setOnErrorListener(this)
            ijkAudioPlayer.player.setOnPreparedListener(this)
//        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val uri = intent?.getStringExtra(PLAY_URI)
        ijkAudioPlayer.setPathAndPrepare(uri)
        ijkAudioPlayer.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.d("onError p1 = $p1 , p2 = $p2")
        return false
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d("onPrepared ")
    }

}