package com.ppx.music.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import com.ppx.music.http.HttpResponse
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：日推的viewmodel
 */
class DailyRecommendViewModel : ViewModel() {

    private val homeRepository: DailyRecommendRepository = DailyRecommendRepository()
    val songDetailInfoList = MutableLiveData<ArrayList<SongDetailInfo>>()

    fun getDailyRecommendSongs(): HttpResponse<List<SongDetailInfo>> {

        /**
         *  一切问题都是从  viewModelScope  这里开始的  哎
         *  先试试retrofit  做网络请求
         */

//        viewModelScope.launch(Dispatchers.Main) {
//            LogUtils.d("日推")
//            homeRepository.getDailyRecommend()
//        }
        return HttpResponse<List<SongDetailInfo>>(0, "", arrayListOf())
    }
}