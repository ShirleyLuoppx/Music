package com.ppx.music.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.PaletteAsyncListener
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.databinding.ActivityMusicPlayerBinding
import com.ppx.music.http.MusicRepository
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.model.SongVipStatus
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.NumberTransUtils
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 音频播放页
 * TODO：歌词+底部小播放器显示
 */
class MusicPlayerActivity : BaseActivity<ActivityMusicPlayerBinding>(), OnClickListener,
    OnSeekBarChangeListener {

    private val TAG = "PlayerActivity"
    private var clickSongDetailInfo: SongDetailInfo? = SongDetailInfo()
    private val musicController = MusicController.instance
    private val musicRepository = MusicRepository()
    private lateinit var stylusRotate: RotateAnimation
    private val recordSet = AnimatorSet()

    //当前播放歌曲的毫秒数
    private var currSongMillsTime = 0

    //发送message id更改播放时间显示和进度条显示
    private val MSG_UPDATE_TIME = 1001
    private lateinit var handler: Handler
    private lateinit var message: Message

    //歌曲播放模式：0列表循环、1单曲循环、2随机播放
    private var playMode = 0

    override fun initView() {
        execAnim()
        handlerUpdateTimeUI()
    }

    override fun initListener() {
        binding.ivLoopMode.setOnClickListener(this)
        binding.ivPreSong.setOnClickListener(this)
        binding.ivPlaySong.setOnClickListener(this)
        binding.ivNextSong.setOnClickListener(this)
        binding.ivPlayingSongList.setOnClickListener(this)
        binding.ivDownWhite.setOnClickListener(this)
        binding.sbSeekbar.setOnSeekBarChangeListener(this)

    }

    @SuppressLint("NewApi")
    override fun initData() {
        EventBus.getDefault().register(this)

        clickSongDetailInfo =
            intent.getParcelableExtra("clickSongDetailInfo")
        LogUtils.d(TAG, "initData clickSongDetailInfo = $clickSongDetailInfo")

        try {
            if (clickSongDetailInfo != null) {
                val clickSongId = clickSongDetailInfo?.songId
                if (clickSongId != null) {
                    lifecycleScope.launch {
                        musicRepository.getSongUrlById(clickSongId)
                    }
                }

                initSongData(clickSongDetailInfo!!)
            }
        } catch (e: Exception) {
            LogUtils.d(TAG, "initData Exception = $e")
            e.printStackTrace()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_music_player
    }

    override fun destroyView() {
        EventBus.getDefault().unregister(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_play_song -> {
                LogUtils.d(
                    TAG,
                    "onclick iv_play_song . playingStatus = ${musicController.isPlaying()}"
                )
                clickBtnPlayPause()
            }

            R.id.iv_loop_mode -> {
                LogUtils.d(TAG, "onclick iv_loop_mode playMode  = $playMode")
                clickBtnMode()
            }

            R.id.iv_pre_song -> {
                LogUtils.d(TAG, "onclick iv_pre_song")
                musicController.playPreSong()

                if (handler.hasMessages(MSG_UPDATE_TIME)) {
                    handler.removeMessages(MSG_UPDATE_TIME)
                }
                handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                binding.ivPlaySong.setImageResource(R.mipmap.ic_pause)

                recordSet.end()
                execAnim()
            }

            R.id.iv_next_song -> {
                LogUtils.d(TAG, "onclick iv_next_song")
                musicController.playNextSong()

                if (handler.hasMessages(MSG_UPDATE_TIME)) {
                    handler.removeMessages(MSG_UPDATE_TIME)
                }
                handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                binding.ivPlaySong.setImageResource(R.mipmap.ic_pause)

                recordSet.end()
                execAnim()
            }

            R.id.iv_playing_song_list -> {
                LogUtils.d(TAG, "onclick iv_playing_song_list}")
            }

            R.id.iv_down_white -> {
                finish()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//        LogUtils.d("progress = $progress , fromUser = $fromUser")
        if (fromUser) {
            musicController.seekTo(progress * 1000)
            currSongMillsTime = progress * 1000
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun clickBtnPlayPause() {
        if (musicController.isPlaying()) {
            musicController.pauseMusic()
            binding.ivPlaySong.setImageResource(R.mipmap.ic_play)
            recordSet.pause()
            stylusAnim(0)
            if (handler.hasMessages(MSG_UPDATE_TIME)) {
                handler.removeMessages(MSG_UPDATE_TIME)
            }
        } else {
            musicController.resumeMusic()
            binding.ivPlaySong.setImageResource(R.mipmap.ic_pause)
            recordSet.resume()
            stylusAnim(1)
            if (handler.hasMessages(MSG_UPDATE_TIME)) {
                handler.removeMessages(MSG_UPDATE_TIME)
            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
        }
        binding.ivStylus.startAnimation(stylusRotate)
    }

    private fun clickBtnMode() {
        //歌曲播放模式：0列表循环、1单曲循环、2随机播放
        when (playMode) {
            0 -> {
                playMode = 1
                binding.ivLoopMode.setImageResource(R.mipmap.ic_playmode_single_loop)
            }

            1 -> {
                playMode = 2
                binding.ivLoopMode.setImageResource(R.mipmap.ic_playmode_random)
            }

            2 -> {
                playMode = 0
                binding.ivLoopMode.setImageResource(R.mipmap.ic_playmode_sequence)
            }
        }
        LogUtils.d(TAG, "after click iv_loop_mode playMode = $playMode")
        musicController.setPlayMode(playMode)
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
                        binding.tvCurrTime.text =
                            NumberTransUtils.long2Minutes(currSongMillsTime.toLong())
                        binding.sbSeekbar.progress = currSongMillsTime / 1000
                        handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000)
                    }
                }
            }
        }

        message = handler.obtainMessage()
        message.what = MSG_UPDATE_TIME
        handler.sendMessage(message)
    }

    private fun initSongData(songData: SongDetailInfo) {
        Glide.with(this).load(songData.picUrl).into(binding.spiMusicRotate)

        binding.tvSongName.text = songData.songName

        val singerSb = StringBuilder()
        songData.songArtists.forEach {
            singerSb.append(it)
            singerSb.append("/")
        }
        binding.tvSingerName.text = singerSb.dropLast(1)

        currSongMillsTime = 0
        binding.sbSeekbar.progress = 0
        binding.sbSeekbar.max = (songData.songTime / 1000).toInt() //歌曲有多少秒 max就是多少  方便计算
        LogUtils.d(TAG, "seekbar max = ${songData.songTime / 1000}")
        val timeStr = NumberTransUtils.long2Minutes(songData.songTime)
        binding.tvCurrTime.text = "00:00"
        binding.tvTotalTime.text = timeStr
        binding.tvVipStatus.visibility =
            if (songData.songVipStatus == SongVipStatus.FREE || songData.songVipStatus == SongVipStatus.ALL_CAN_PLAY) View.GONE else View.VISIBLE

//        getMainColor()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSongDetailInfo(songDetailInfo: SongDetailInfo) {
        initSongData(songDetailInfo)
    }

    /**
     * status = 1 唱针下拉
     * status = 0 唱针上拉
     */
    private fun stylusAnim(status: Int) {
        if (status == 1) {
            stylusRotate = RotateAnimation(
                0f,
                30f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            )
        } else {
            stylusRotate = RotateAnimation(
                30f,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f
            )
        }
        val lin = LinearInterpolator()
        stylusRotate.interpolator = lin
        stylusRotate.duration = 1500 //设置动画持续时间
        stylusRotate.repeatCount = 0 //设置重复次数
        stylusRotate.fillAfter = true //动画执行完后是否停留在执行完的状态
    }

    /**
     * 执行动画，唱片和唱针的动画
     */
    private fun execAnim() {
        // 创建从0度到360度的旋转动画，耗时3秒
        val rotateAnimator = ObjectAnimator.ofFloat(binding.spiMusicRotate, "rotation", 0f, 360f)
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE // 无限循环播放
        rotateAnimator.interpolator = LinearInterpolator() // 匀速旋转
        rotateAnimator.setDuration(20 * 1000)
        // 创建AnimatorSet来管理rotateAnimator
        recordSet.play(rotateAnimator)
        recordSet.start()

        stylusAnim(1)
        binding.ivStylus.animation = stylusRotate
        binding.ivStylus.startAnimation(stylusRotate)
    }

    override fun onResume() {
        super.onResume()
        getMainColor()
    }

    //通过palette 获取歌曲封面的主色调设置为本activity的背景色
    private fun getMainColor() {

        val drawable = binding.spiMusicRotate.drawable
        if (drawable != null) {
            val bitmap = drawable.toBitmap()
            if (bitmap != null) {
                // 异步方式获取
                Palette.from(bitmap).generate { palette ->

                    LogUtils.d(TAG, "getMainColor palette = $palette")

                    if (palette != null) {
                        val vibrant = palette.vibrantSwatch //有活力的
                        val vibrantDark = palette.darkVibrantSwatch //有活力的暗色
                        val vibrantLight = palette.lightVibrantSwatch //有活力的亮色

                        val muted = palette.mutedSwatch //柔和的
                        val mutedDark = palette.darkMutedSwatch //柔和的暗色
                        val mutedLight = palette.lightMutedSwatch //柔和的亮色

                        val population = vibrant?.population //样本中的像素数量
                        val rgb = vibrant?.rgb //颜色的RBG值
                        val hsl = vibrant?.getHsl() //颜色的HSL值
                        val bodyTextColor = vibrant?.getBodyTextColor() //主体文字的颜色值
                        val titleTextColor = vibrant?.getTitleTextColor() //标题文字的颜色值

                        binding.tvSingerName.setBackgroundColor(rgb!!)
                        LogUtils.d(TAG, "palette rgb = $rgb")

                    } else {
                        LogUtils.d(TAG, "palette is null")
                    }
                }
            } else {
                LogUtils.d(TAG, "getMainColor bitmap is null")
            }
        } else {
            LogUtils.d(TAG, "getMainColor drawable is null")
        }
    }

}

