package com.ppx.music.mvvm

import com.ppx.music.http.HttpResponse
import com.ppx.music.model.SongDetailInfo
import retrofit2.http.GET

/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：
 */
interface HttpService {
    companion object {
        const val BASE_URL = "http://119.91.147.152:3000"
    }

    @GET("/recommend/songs")
    suspend fun getCollectArticles(): HttpResponse<String>


}