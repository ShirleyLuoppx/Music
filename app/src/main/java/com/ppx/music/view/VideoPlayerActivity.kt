package com.ppx.music.view

import android.net.Uri
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.VideoView
import com.ppx.music.R
import com.ppx.music.databinding.ActivityVideoPlayerBinding
import com.ppx.music.player.MyIjkVideoView
import com.ppx.music.utils.LogUtils
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 *
 * @Author Shirley
 * @Date：2024/10/16
 * @Desc：视频播放页
 */
class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(), SurfaceHolder.Callback,
    OnPreparedListener, OnCompletionListener, OnErrorListener {

    private val TAG = "VideoPlayerActivity"
    private var videoPath = ""
    private var mVideoView: VideoView? = null
    private var ijkVideoView: MyIjkVideoView? = null
    private var surfaceView: SurfaceView? = null
    private val ijkMediaPlayer = IjkMediaPlayer()

    override fun getLayoutId(): Int {
        return R.layout.activity_video_player
    }

    override fun initView() {
        mVideoView = binding.vvVideoview
        ijkVideoView = binding.ijkVideo
        surfaceView = binding.sfSurfaceview
    }

    override fun initListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        surfaceView?.holder?.addCallback(this)
        ijkMediaPlayer.setOnPreparedListener(this)
        ijkMediaPlayer.setOnCompletionListener(this)
        ijkMediaPlayer.setOnErrorListener(this)
    }

    override fun initData() {
        videoPath = intent.getStringExtra("playUrl").toString()
        LogUtils.d(TAG, "initData: videoPath: $videoPath")
    }

    override fun destroyView() {
        if (ijkMediaPlayer.isPlaying) {
            ijkMediaPlayer.stop()
        }
        ijkMediaPlayer.reset()
        ijkMediaPlayer.release()
    }

    override fun onStop() {
        super.onStop()
        mVideoView?.stopPlayback()
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        LogUtils.d(TAG, "surfaceCreated: p0: $p0")
        ijkMediaPlayer.reset()
        ijkMediaPlayer.setDisplay(p0)
        ijkMediaPlayer.setDataSource(this, Uri.parse(videoPath))
        ijkMediaPlayer.prepareAsync()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        LogUtils.d(TAG, "surfaceChanged: p1: $p1, p2: $p2, p3: $p3")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        LogUtils.d(TAG, "surfaceDestroyed: ")
        ijkMediaPlayer.stop()
        ijkMediaPlayer.release()
    }

    override fun onPrepared(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onPrepared: ")
        // 播放器准备完成回调，可以开始播放
        ijkMediaPlayer.start()
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        LogUtils.d(TAG, "onCompletion: ")
    }

    override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
        LogUtils.d(TAG, "onError: p1: $p1, p2: $p2")
        return false
    }

}