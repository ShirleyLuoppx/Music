package com.ppx.music.view.recommend

import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendPlayListAdapter
import com.ppx.music.databinding.FragmentRecommendBinding
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.view.BaseFragment
import kotlinx.coroutines.launch

/**
 * 音乐首页：
 *
 * 包含 ：每日30首，推荐歌单，猜你喜欢，排行榜等列表
 */
class RecommendFragment : BaseFragment<FragmentRecommendBinding>(), OnClickListener {

    private val TAG = "RecommendFragment"
    private val musicController = MusicController.instance
    private val dailyRecommendPlayListAdapter = DailyRecommendPlayListAdapter()

    override fun getLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initView() {
        setGreetingWords()
    }

    override fun initData() {
        initRV()
        getDailyRecommendPlayList()
    }

    override fun initListener() {
        binding.cvDailyRecommend.setOnClickListener(this)
    }

    override fun onDestroyFragment() {
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cv_daily_recommend -> {
                clickDailyRecommend()
                return
            }
        }
    }

    private fun clickDailyRecommend() {
        //转到DailyRecommendFragment
        val dailyRecommendFragment = DailyRecommendFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.framelayout, dailyRecommendFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun setGreetingWords() {
        //获取当前的时间
        val currentTime = System.currentTimeMillis()
        //将时间戳转换为日期格式
        val date = java.util.Date(currentTime)
        //获取小时
        val hour = date.hours
        //根据小时设置问候语
        if (hour in 6..12) {
            binding.tvGreeting.text = "早上好"
        } else if (hour in 13..18) {
            binding.tvGreeting.text = "下午好"
        } else {
            binding.tvGreeting.text = "晚上好"
        }
    }

    private fun getDailyRecommendPlayList() {
        lifecycleScope.launch {
            val playListData = musicController.getDailyRecommendPlayList()
            LogUtils.d(TAG, "getDailyRecommendPlayList playListData = ${playListData[0].name}")
            dailyRecommendPlayListAdapter.addAll(playListData)
        }
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendPlaylist.layoutManager = layoutManager
        binding.rvRecommendPlaylist.adapter = dailyRecommendPlayListAdapter
    }
}