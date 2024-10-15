package com.ppx.music.http

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @Author Shirley
 * @Date：2024/10/11
 * @Desc：
 */
object NetworkModule {

    private const val BASE_URL = "http://119.91.147.152:3000"

    fun createRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        // 返回一个 retrofit 实例
        return Retrofit.Builder()
            .client(okHttpClient) // 让 retrofit 使用 okhttp
            .baseUrl(BASE_URL) // api 地址
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))// 使用 gson 解析 json
            .build()
    }

    /**
     * TODO 创建 OkHttpClient 实例
     */
    fun createOkHttpClient() : OkHttpClient {
        // 返回一个 OkHttpClient 实例
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}