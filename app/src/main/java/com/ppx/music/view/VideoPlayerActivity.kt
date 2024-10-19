package com.ppx.music.view

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.ppx.music.R
import com.ppx.music.databinding.ActivityVideoPlayerBinding
import com.ppx.music.player.VideoController
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/10/16
 * @Desc：视频播放页
 */
class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(), SurfaceHolder.Callback,
    OnClickListener {


    private var videoPath = ""
    private var surfaceView: SurfaceView? = null
    private var screenWidth = 0
    private var screenHeight = 0

    //TODO：ijkMediaPlayer的逻辑  单独拿出来写一个VideoPlayer
    private var videoController: VideoController? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_video_player
    }

    private val TAG = "VideoPlayerActivity"

    override fun initView() {
        surfaceView = binding.sfSurfaceview
    }

    override fun initListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        surfaceView?.holder?.addCallback(this)
        binding.ivPause.setOnClickListener(this)
    }

    override fun initData() {
        videoPath = intent.getStringExtra("playUrl").toString()
        LogUtils.d(TAG, "initData: videoPath: $videoPath")
        videoController = VideoController()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_pause -> {
                videoController?.pauseOrStart()
            }
        }
    }

    override fun destroyView() {
        videoController?.release()
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        videoController?.setDataSource(p0, videoPath)

        screenWidth = windowManager.defaultDisplay.width
        screenHeight = windowManager.defaultDisplay.height
        LogUtils.d(TAG, "surfaceCreated: screenWidth: $screenWidth, screenHeight: $screenHeight")
        setRate(1)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        LogUtils.d(TAG, "surfaceChanged: p1: $p1, p2: $p2, p3: $p3")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        LogUtils.d(TAG, "surfaceDestroyed: ")
        videoController?.release()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtils.d(TAG, "onConfigurationChanged: ${newConfig.orientation}")
        LogUtils.d(
            TAG,
            "onConfigurationChanged: ${ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE}"
        ) //竖屏:SCREEN_ORIENTATION_PORTRAIT = 0   SCREEN_ORIENTATION_LANDSCAPE = 1横屏
        setRate(newConfig.orientation)
    }

    /**
     * @param orientation 1 竖屏 2 横屏
     */
    private fun setRate(orientation: Int) {
        LogUtils.d(TAG, "setRate: orientation: $orientation")
        //初始化进来设置surfaceView.width=屏幕的宽，高=屏幕宽的    3:4     16:9
        LogUtils.d(TAG, "setRate: screenWidth: $screenWidth, screenHeight: $screenHeight")

        setSystemUIStatus(orientation == 1)

        if (orientation == 1) {
            surfaceView?.layoutParams?.width = screenWidth
            surfaceView?.layoutParams?.height = screenWidth * 9 / 16  //0.5625   0.75
        } else if (orientation == 2) {
            surfaceView?.layoutParams?.width = screenWidth * 16 / 9
            surfaceView?.layoutParams?.height = screenWidth  //0.5625   0.75
            LogUtils.d(
                TAG,
                "setRate: when orientation == land height = ${surfaceView?.layoutParams?.height}"
            )
        }
        surfaceView?.requestLayout()
    }

    /**
     * 设置状态栏和导航栏的显示和隐藏
     */
    private fun setSystemUIStatus(visible: Boolean) {
        //设置非全屏时候的状态栏和导航栏  显示出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(visible)
            val controller = window.insetsController
            if (controller != null) {
                if (visible) {
                    controller.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
                } else {
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }
}