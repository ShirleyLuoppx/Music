package com.ppx.music.http

import com.alibaba.fastjson.JSONObject
import com.ppx.music.model.SongDetailInfo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * @Author Shirley
 * @Date：2024/10/11
 * @Desc：
 */
interface NetworkService {

    //http://119.91.147.152:3000/song/url?id=2602622157

    @GET("song/url")
    suspend fun getNewsService(
        @Query(value = "id") id : String
    ) : JSONObject

    /**
     * TODO 通过 Retrofit 创建一个 NetworkService 实例
     */
    companion object{
        fun createService() : NetworkService {
            return NetworkModule.createRetrofit(NetworkModule.createOkHttpClient())
                .create(NetworkService::class.java) // TODO 返回一个 NetworkService 的实例
        }
    }
}