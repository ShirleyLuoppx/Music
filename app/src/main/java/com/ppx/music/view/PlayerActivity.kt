package com.ppx.music.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ppx.music.MusicApplication
import com.ppx.music.R
import com.ppx.music.databinding.FragmentPlayerBinding
import com.ppx.music.model.PlaySongUrlEvent
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.player.MusicController
import com.ppx.music.player.MusicPlayerService
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.TimeTransUtils
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 播放页
 */
class PlayerActivity : BaseActivity<FragmentPlayerBinding>(), OnClickListener,
    OnSeekBarChangeListener {
    private val TAG = "PlayerActivity"
    private var clickSongDetailInfo: SongDetailInfo? = SongDetailInfo()
    private val musicController = MusicController.instance
    private lateinit var stylusRotate: RotateAnimation
    private lateinit var rotateAnimator: ObjectAnimator
    private val recordSet = AnimatorSet()

    //当前播放歌曲的毫秒数
    private var currSongMillsTime = 0
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

        if (clickSongDetailInfo != null) {
            val clickSongId = clickSongDetailInfo?.songId
            if (clickSongId != null) {
                lifecycleScope.launch {
                    musicController.getSongUrlById(clickSongId)
                }
            }

            initSongData(clickSongDetailInfo!!)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_player
    }

    override fun destroyView() {
        EventBus.getDefault().unregister(this)
    }

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
            }

            R.id.iv_next_song -> {
                LogUtils.d(TAG, "onclick iv_next_song")
                musicController.playNextSong()
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
            handler.removeMessages(1001)
        } else {
            musicController.resumeMusic()
            binding.ivPlaySong.setImageResource(R.mipmap.ic_pause)
            recordSet.resume()
            stylusAnim(1)
            handler.sendEmptyMessageDelayed(1001, 1000)
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
                    1001 -> {
//                        LogUtils.d("handleMessage currSongMillsTime = $currSongMillsTime")
                        currSongMillsTime += 1000
                        binding.tvCurrTime.text =
                            TimeTransUtils.long2Minutes(currSongMillsTime.toLong())
                        binding.sbSeekbar.progress = currSongMillsTime / 1000
                        handler.sendEmptyMessageDelayed(1001, 1000)
                    }
                }
            }
        }

        message = handler.obtainMessage()
        message.what = 1001
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
        val timeStr = TimeTransUtils.long2Minutes(songData.songTime)
        binding.tvCurrTime.text = "00:00"
        binding.tvTotalTime.text = timeStr
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

    //执行动画，唱片和唱针的动画
    private fun execAnim() {
        // 创建从0度到360度的旋转动画，耗时3秒
        rotateAnimator = ObjectAnimator.ofFloat(binding.spiMusicRotate, "rotation", 0f, 360f)
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


}

