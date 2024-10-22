package com.ppx.music.view.recommend

import android.annotation.SuppressLint
import android.content.Intent
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendSongAdapter
import com.ppx.music.databinding.FragmentDailyRecommendBinding
import com.ppx.music.http.MusicRepository
import com.ppx.music.player.MusicController
import com.ppx.music.utils.CommonUtils
import com.ppx.music.utils.LogUtils
import com.ppx.music.view.BaseFragment
import com.ppx.music.view.MusicPlayerActivity
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：每日推荐歌曲列表
 */
class DailyRecommendSongFragment : BaseFragment<FragmentDailyRecommendBinding>() {


    private val dailyRecommendAdapter = DailyRecommendSongAdapter()
    private val musicRepository = MusicRepository()

    override fun initView() {

        initRv()
    }

    override fun initListener() {

        dailyRecommendAdapter.setOnItemClickListener { adapter, view, position ->

            MusicController.instance.setCurrentSongIndex(position)

            val clickSongDetailInfo = adapter.getItem(position)
            val clickSongId = clickSongDetailInfo?.songId
            val clickSongName = clickSongDetailInfo?.songName
            LogUtils.d("onItemClick $position and clickSongId = $clickSongId and clickSongName = $clickSongName")

            val intent = Intent(requireActivity(), MusicPlayerActivity::class.java)
            intent.putExtra("clickSongDetailInfo", clickSongDetailInfo)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            fragmentManager?.popBackStack()
            transaction?.commit()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        lifecycleScope.launch {
            val data = musicRepository.getDailyRecommendSongList()
            dailyRecommendAdapter.addAll(data)

            Glide.with(this@DailyRecommendSongFragment)
                .load(data[Random.nextInt(0, data.size)].picUrl)
                .into(binding.ivTopBg)
        }

        setDate()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_daily_recommend
    }

    override fun onDestroyFragment() {
    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvDailyRecommend.layoutManager = layoutManager
        binding.rvDailyRecommend.adapter = dailyRecommendAdapter
    }

    private fun setDate() {
        val month = CommonUtils.getDate()[1]
        val day = CommonUtils.getDate()[2]
        val sbString = SpannableString("$month / $day")
        sbString.setSpan(
            AbsoluteSizeSpan(50, true),
            0,
            month.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
        sbString.setSpan(
            AbsoluteSizeSpan(13, true),
            month.length,
            day.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.tvDate.text = sbString
    }


}