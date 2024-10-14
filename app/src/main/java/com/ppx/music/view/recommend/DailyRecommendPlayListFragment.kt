package com.ppx.music.view.recommend

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.databinding.FragmentDailyRecommendPlayListBinding
import com.ppx.music.model.PlayListInfo
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.NumberTransUtils
import com.ppx.music.view.BaseFragment
import java.math.BigDecimal

/**
 *
 * @Author Shirley
 * @Date：2024/10/14
 * @Desc：每日推荐歌单列表
 */
class DailyRecommendPlayListFragment : BaseFragment<FragmentDailyRecommendPlayListBinding>() {

    private val TAG = "DailyRecommendPlayListFragment"
    private val musicController = MusicController.instance
    private var playListInfo: PlayListInfo? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_daily_recommend_play_list
    }

    override fun initView() {

    }

    override fun initData() {
        playListInfo = musicController.getPlayListInfo()
        if (playListInfo != null) {
            binding.tvPlayListName.text = playListInfo?.name
            binding.tvPlayCount.text =
                NumberTransUtils.formatNumber(BigDecimal(playListInfo?.playcount.toString()).toPlainString())
//            binding.cvPlaylistInfo.background = Drawable.createFromPath(playListInfo?.picUrl)
            binding.tvPlayListCreatorNickname.text = playListInfo?.creator?.name
            context?.let {
                Glide.with(it).load(playListInfo?.creator?.avatarUrl)
                    .into(binding.ivPlaylistCreatorHeadimg)
            }
            context?.let {
                Glide.with(it).load(playListInfo?.picUrl)
                    .into(binding.ivPlaylistBg)
            }
        } else {
            LogUtils.d(TAG, "initData: playListInfo is null")
        }
    }

    override fun initListener() {
    }

    override fun onDestroyFragment() {
    }
}