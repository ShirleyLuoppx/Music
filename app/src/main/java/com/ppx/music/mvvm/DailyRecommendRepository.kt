package com.ppx.music.mvvm

import com.ppx.music.http.HttpResponse
import com.ppx.music.http.RetrofitClient
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils


/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：
 */
class DailyRecommendRepository {

    val TAG = "DailyRecommendRepository"

    suspend fun getDailyRecommend(): HttpResponse<List<SongDetailInfo>> {
        val responseStr = RetrofitClient.service.getCollectArticles()
        LogUtils.d("$TAG getDailyRecommend responseStr = $responseStr")
        //TODO  解析数据，返回给viewmodel，再由viewmodel  post出去给fragment，然后fragment来observe监听数据，显示到页面上
        return HttpResponse<List<SongDetailInfo>>(0,"", arrayListOf())
    }



}