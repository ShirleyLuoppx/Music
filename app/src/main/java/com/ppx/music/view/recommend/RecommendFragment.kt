package com.ppx.music.view.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ppx.music.R
import com.ppx.music.databinding.FragmentRecommendBinding


/**
 * 音乐首页：
 *
 * 包含 ：每日30首，推荐歌单，猜你喜欢，排行榜等列表
 */
class RecommendFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentRecommendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recommend, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initListener()
    }

    private fun initData() {

    }

    private fun initListener() {
        binding.cvDailyRecommend.setOnClickListener(this)
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


}