package com.ppx.music.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.common.Constants
import com.ppx.music.databinding.ActivityVideoPlayerBinding
import com.ppx.music.model.MvInfo
import com.ppx.music.player.VideoController
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.NumberTransUtils

/**
 *
 * @Author Shirley
 * @Date：2024/10/16
 * @Desc：视频播放页
 */
class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(), SurfaceHolder.Callback,
    OnClickListener,OnSeekBarChangeListener {

    private val TAG = "VideoPlayerActivity"
    private var mvInfo: MvInfo? = null
    private var videoPath = ""
    private var surfaceView: SurfaceView? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var videoController: VideoController? = null
    private var isFullScreen: Boolean = false

    //当前播放歌曲的毫秒数
    private var currSongMillsTime = 0

    //发送message id更改播放时间显示和进度条显示
    private val MSG_UPDATE_TIME = 1001
    private lateinit var handler: Handler
    private lateinit var message: Message

    override fun getLayoutId(): Int {
        return R.layout.activity_video_player
    }

    override fun initView() {
        surfaceView = binding.sfSurfaceview
        handlerUpdateTimeUI()
    }

    override fun initListener() {
        binding.ivBack.setOnClickListener(this)
        surfaceView?.holder?.addCallback(this)
        binding.ivPause.setOnClickListener(this)
        binding.ivFullscreen.setOnClickListener(this)
        surfaceView?.setOnClickListener(this)
        binding.sbVideoSeekbar.setOnSeekBarChangeListener(this)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        videoController = VideoController()

        mvInfo = intent.getParcelableExtra(Constants.MV_INFO) as MvInfo?
        if (mvInfo != null) {
            videoPath = mvInfo?.playUrl.toString()
            LogUtils.d(TAG, "initData: videoPath: $videoPath")
            Glide.with(this).load(mvInfo?.cover).into(binding.ivMvCover)
            binding.tvSongSingerName.text = mvInfo?.songName + " - " + mvInfo?.singerName
            binding.tvSongName.text = mvInfo?.songName
            binding.tvSingerName.text = mvInfo?.singerName
            binding.sbVideoSeekbar.progress = 0
            binding.sbVideoSeekbar.max = (mvInfo?.duration!! / 1000)
        } else {
            LogUtils.d(TAG, "initData: mvInfo is null")
        }

        binding.tvSongSingerName.isSelected = true
        binding.tvSongSingerName.requestFocus()

        //TODO:所有的动画都可以单独抽取出来，放到一个动画的工具类里面
        val rotateAnimator = ObjectAnimator.ofFloat(binding.ivMvCover, "rotation", 0f, 360f)
        rotateAnimator.duration = 10 * 1000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_back -> {
                if (isFullScreen) {
                    setRate(1)
                } else {
                    finish()
                }
            }

            R.id.iv_pause -> {
                videoController?.pauseOrStart()
            }

            R.id.iv_fullscreen -> {
                if (isFullScreen) {
                    setRate(1)
                } else {
                    setRate(2)
                }
            }

            R.id.sf_surfaceview -> {
                videoController?.pauseOrStart()
                if (videoController?.isPlaying() == true) {
                    binding.ivPauseWhenClick.visibility = View.VISIBLE
                    if (handler.hasMessages(MSG_UPDATE_TIME)) {
                        handler.removeMessages(MSG_UPDATE_TIME)
                    }

                } else {
                    binding.ivPauseWhenClick.visibility = View.GONE
                    if (handler.hasMessages(MSG_UPDATE_TIME)) {
                        handler.removeMessages(MSG_UPDATE_TIME)
                    }
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                }
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
        )
        setRate(newConfig.orientation)
    }

    override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            videoController?.seekTo(progress * 1000)
            currSongMillsTime = progress * 1000
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
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
            isFullScreen = false
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            surfaceView?.layoutParams?.width = screenWidth
            surfaceView?.layoutParams?.height = screenWidth * 9 / 16  //0.5625   0.75
            binding.clVideoInfo.visibility = View.VISIBLE
            binding.ivFullscreen.setImageResource(R.mipmap.ic_fullscreen)

        } else if (orientation == 2) {
            isFullScreen = true
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            surfaceView?.layoutParams?.width = screenWidth * 16 / 9
            surfaceView?.layoutParams?.height = screenWidth  //0.5625   0.75
            binding.clVideoInfo.visibility = View.GONE
            binding.ivFullscreen.setImageResource(R.mipmap.ic_not_fullscreen)
            LogUtils.d(
                TAG,
                "setRate: when orientation == land height = ${surfaceView?.layoutParams?.height}"
            )
        }
        surfaceView?.requestLayout()
    }

    /**
     * 横屏的时候：隐藏状态栏和导航栏
     * 竖屏的时候：显示状态栏和导航栏，并设置状态栏和导航栏的背景色为黑色
     */
    private fun setSystemUIStatus(visible: Boolean) {
        //设置状态栏和导航栏的背景色为黑色
        if (visible) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black) // 设置状态栏颜色
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black) // 设置导航栏颜色
        }


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

    /**
     * 更新当前播放时间、和progress进度显示
     */
    private fun handlerUpdateTimeUI() {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    MSG_UPDATE_TIME -> {
//                        LogUtils.d("handleMessage currSongMillsTime = $currSongMillsTime")
                        currSongMillsTime += 1000
//                        binding.tvCurrTime.text =
//                            NumberTransUtils.long2Minutes(currSongMillsTime.toLong())
                        binding.sbVideoSeekbar.progress = currSongMillsTime / 1000
                        handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                    }
                }
            }
        }

        message = handler.obtainMessage()
        message.what = MSG_UPDATE_TIME
        handler.sendMessage(message)
    }

}