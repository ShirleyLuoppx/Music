package com.ppx.music.view

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.fastjson.JSONObject
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.databinding.FragmentRecommendBinding
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


/**
 * 音乐首页：
 *
 * 包含 ：每日30首，推荐歌单，猜你喜欢，排行榜等列表
 */
class RecommendFragment : Fragment() {

    private lateinit var binding: FragmentRecommendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_recommend, container, false)
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
        binding.cvDailyRecommend.setOnClickListener {

            //转到DailyRecommendFragment
            val dailyRecommendFragment = DailyRecommendFragment()

//            val fragmentTransaction = fragmentManager?.beginTransaction()
//            fragmentTransaction?.add(R.id.fl_recommend, dailyRecommendFragment)
//            fragmentTransaction?.commit()
//
//
//            val transaction = fragmentManager?.beginTransaction()
//            transaction?.hide(this)
//            transaction?.show(dailyRecommendFragment)
//            transaction?.commit()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_recommend, dailyRecommendFragment)
                ?.hide(this)
                ?.addToBackStack(null)
                ?.commit()

        }

    }


}