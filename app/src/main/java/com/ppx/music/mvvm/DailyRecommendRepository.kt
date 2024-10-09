package com.ppx.music.mvvm

import com.ppx.music.http.RetrofitClient

/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：
 */
class DailyRecommendRepository {


    suspend fun getDailyRecommend(){
        getDailyRecommendSongs()
    }

    private suspend fun getDailyRecommendSongs() {
        RetrofitClient.service.getCollectArticles()
    }
}