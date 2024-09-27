package com.ppx.music.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.bumptech.glide.Glide
import com.ppx.music.MusicApplication
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.databinding.FragmentPlayerBinding
import com.ppx.music.model.PlaySongUrlEvent
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.player.MusicController
import com.ppx.music.player.MusicPlayerService
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.TimeTransUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PlayerActivity : BaseActivity<FragmentPlayerBinding>(), OnClickListener {

    private var clickSongDetailInfo: SongDetailInfo? = SongDetailInfo()
    private val musicController = MusicController.instance
    private lateinit var stylusRotate: RotateAnimation
    private lateinit var rotateAnimator: ObjectAnimator
    private val recordSet = AnimatorSet()

    override fun initView() {
        // 创建从0度到360度的旋转动画，耗时3秒
        rotateAnimator = ObjectAnimator.ofFloat(binding.spiMusicRotate, "rotation", 0f, 360f)
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE // 无限循环播放
        rotateAnimator.interpolator = LinearInterpolator() // 匀速旋转
        rotateAnimator.setDuration(20*1000)
        // 创建AnimatorSet来管理rotateAnimator
        recordSet.play(rotateAnimator)
        recordSet.start()

        stylusAnim(1)
        binding.ivStylus.animation = stylusRotate
        binding.ivStylus.startAnimation(stylusRotate)
    }

    override fun initListener() {
        binding.ivLoopMode.setOnClickListener(this)
        binding.ivPreSong.setOnClickListener(this)
        binding.ivPlaySong.setOnClickListener(this)
        binding.ivNextSong.setOnClickListener(this)
        binding.ivPlayingSongList.setOnClickListener(this)

    }

    @SuppressLint("NewApi")
    override fun initData() {
        EventBus.getDefault().register(this)

        clickSongDetailInfo =
            intent.getParcelableExtra("clickSongDetailInfo")
        LogUtils.d("initData clickSongDetailInfo = $clickSongDetailInfo")

        if (clickSongDetailInfo != null) {
            val clickSongId = clickSongDetailInfo?.songId
            if (clickSongId != null) {
                getSongUrlById(clickSongId)
            }

            initPlayerData(clickSongDetailInfo!!)
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
                LogUtils.d("onclick iv_play_song . playingStatus = ${musicController.isPlaying()}")
                if (musicController.isPlaying()) {
                    musicController.pauseMusic()
                    binding.ivPlaySong.setImageResource(R.mipmap.ic_play)
                    recordSet.pause()
                    stylusAnim(0)
                } else {
                    musicController.resumeMusic()
                    binding.ivPlaySong.setImageResource(R.mipmap.ic_pause)
                    recordSet.resume()
                    stylusAnim(1)
                }
                binding.ivStylus.startAnimation(stylusRotate)
            }

            R.id.iv_loop_mode -> {
                LogUtils.d("onclick iv_loop_mode}")
            }

            R.id.iv_pre_song -> {
                LogUtils.d("onclick iv_pre_song}")
            }

            R.id.iv_next_song -> {
                LogUtils.d("onclick iv_next_song}")
            }

            R.id.iv_playing_song_list -> {
                LogUtils.d("onclick iv_playing_song_list}")
            }
        }
    }

    private fun getSongUrlById(id: String) {
        LogUtils.d("PlayerActivity getSongUrlById id = $id")
        NetRequest.instance.getSongUrlById(id)
    }

    private fun initPlayerData(songData: SongDetailInfo) {
        Glide.with(this).load(songData.picUrl).into(binding.spiMusicRotate)

        binding.tvSongName.text = songData.songName

        val singerSb = StringBuilder()
        songData.songArtists.forEach {
            singerSb.append(it)
            singerSb.append("/")
        }
        binding.tvSingerName.text = singerSb.dropLast(1)

        val timeStr = TimeTransUtils.long2Minutes(songData.songTime)
        binding.tvTotalTime.text = timeStr
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlaySongUrlEvent(event: PlaySongUrlEvent) {
        LogUtils.d("onPlaySongUrlEvent url = ${event.url}")
        if (!TextUtils.isEmpty(event.url)) {
            //使用豆包提供的原生的mediaPlayer也可以正常播放...
            val intent = Intent(MusicApplication.context, MusicPlayerService::class.java)
            intent.putExtra("url", event.url)
            intent.setAction("PLAY")
            startService(intent)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSongDetailInfo(songDetailInfo: SongDetailInfo) {
        initPlayerData(songDetailInfo)
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

}