package com.ppx.music.mvvm

import androidx.lifecycle.ViewModel
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：日推的viewmodel
 */
class DailyRecommendViewModel (private val homeRepository: DailyRecommendRepository) : ViewModel(){


    fun getDailyRecommendSongs() {
        homeRepository.getDailyRecommend()
        LogUtils.d("日推")
    }
}