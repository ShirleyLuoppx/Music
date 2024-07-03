package com.ppx.music.utils

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.create
import okio.IOException


/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：okhttp网络请求工具类
 */
class NetUtils {

    companion object {
        private lateinit var netUtils: NetUtils
        fun getInstance(): NetUtils {
            if (netUtils == null) {
                netUtils = NetUtils()
            }
            return netUtils
        }
    }

    fun postString(apiUrl: String, requestBody: String) {
        val mediaType: MediaType? = "text/x-markdown; charset=utf-8".toMediaTypeOrNull()
        val request: Request = Request.Builder()
            .url(apiUrl)
            .post(create(mediaType, requestBody))
            .build()
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("onFailure: " + e.message.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("response.protocol = " + response.protocol)
                LogUtils.d((response.code).toString() + " " + response.message)
                val headers: Headers = response.headers
                for (i in 0 until headers.size) {
                    LogUtils.d(headers.name(i) + ":" + headers.value(i))
                }
                LogUtils.d("onResponse: " + response.body?.string())
            }
        })
    }

    fun postForms(apiUrl: String, key: String, value: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add(key, value)
            .build()
        val request: Request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("onFailure: " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("response.protocol = " + response.protocol)
                LogUtils.d((response.code).toString() + " " + response.message)
                val headers = response.headers
                for (i in 0 until headers.size) {
                    LogUtils.d(headers.name(i) + ":" + headers.value(i))
                }
                LogUtils.d("onResponse: " + response.body!!.string())
            }
        })
    }
}