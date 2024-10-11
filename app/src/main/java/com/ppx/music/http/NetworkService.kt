package com.ppx.music.http

import com.alibaba.fastjson.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * @Author Shirley
 * @Date：2024/10/11
 * @Desc：
 */
interface NetworkService {

    @POST("captcha/sent")
    suspend fun sendVerifyCode(
        @Query(value = "phone") phone: String,
        @Query(value = "timestamp") timestamp: String
    ): String

    @GET("song/url")
    suspend fun getNewsService(@Query(value = "id") id: String): JSONObject

    @POST("captcha/verify")
    suspend fun checkVerifyCode(
        @Query(value = "phone") phone: String,
        @Query(value = "captcha") captcha: String,
        @Query(value = "timestamp") timestamp: String
    ): String

    @POST("cellphone/existence/check")
    suspend fun checkPhone(
        @Query(value = "phone") phone: String,
        @Query(value = "timestamp") timestamp: String
    ): String

    @GET("user/detail")
    suspend fun getUserDetail(@Query(value = "uid") uid: String): String

    @GET("get/userids")
    suspend fun getUserIdByNickName(@Query(value = "nicknames") nicknames: String): String

    @GET("recommend/songs")
    suspend fun getDailyRecommendSongs(): String


    /**
     * TODO 通过 Retrofit 创建一个 NetworkService 实例
     */
    companion object {
        fun createService(): NetworkService {
            return NetworkModule.createRetrofit(NetworkModule.createOkHttpClient())
                .create(NetworkService::class.java) // TODO 返回一个 NetworkService 的实例
        }
    }
}