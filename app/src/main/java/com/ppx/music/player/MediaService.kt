package com.ppx.music.player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import com.ppx.music.MusicApplication
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class MediaService : Service() ,OnErrorListener,OnPreparedListener {

    //ijkplayer
    private var ijkAudioPlayer  = IjkMediaPlayer()
    private val TAG = "MediaService"


    companion object {
        val PLAY_URI = "PLAY_URI"
        val PLAY = "PLAY"

        fun startPlay(context: Context, uri: String) {
            context.startService(
                Intent(context, MediaService::class.java).putExtra(PLAY_URI, uri)   //.setAction(PLAY)
            )
        }

    }

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


        /*if (ijkAudioPlayer == null) {
            ijkAudioPlayer = IjkPlayer()
            ijkAudioPlayer?.init()
            ijkAudioPlayer?.getPlayer()?.setOnErrorListener(this)
            ijkAudioPlayer?.getPlayer()?.setOnPreparedListener(this)
        }*/

        LogUtils.d("$TAG   onCreate service ====================================== ijkAudioPlayer = $ijkAudioPlayer")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        LogUtils.d("$TAG onStartCommand ====================================== ")
        val uri = intent?.getStringExtra(PLAY_URI)
        LogUtils.d("$TAG onStartCommand uri = $uri")
//                    ijkAudioPlayer.stop()
        LogUtils.d("111111111111111111111111111111111111111111 $ijkAudioPlayer")
        ijkAudioPlayer.setDataSource(MusicApplication.context, Uri.parse(uri))
        LogUtils.d("22222222222222222222222222222222222222222 $ijkAudioPlayer")
        ijkAudioPlayer.prepareAsync()
        LogUtils.d("333333333333333333333333333333333333333333 $ijkAudioPlayer")
        ijkAudioPlayer.start()
        LogUtils.d("44444444444444444444444444444444444444444444 $ijkAudioPlayer")


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.d("onError p1 = $p1 , p2 = $p2")
        return false
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d("onPrepared ======================================")
    }

}