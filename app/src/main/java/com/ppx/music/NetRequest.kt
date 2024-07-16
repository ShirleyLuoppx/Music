package com.ppx.music

import com.ppx.music.common.ApiConstants
import com.ppx.music.utils.LogUtils
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
class NetRequest {

    companion object {
        private lateinit var netUtils: NetRequest
        fun getInstance(): NetRequest {
            if (netUtils == null) {
                netUtils = NetRequest()
            }
            return netUtils
        }
    }

    /**
     * 获取手机验证码
     */
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
                LogUtils.d("onResponse: response.protocol = " + response.protocol)
                LogUtils.d("onResponse:" + (response.code).toString() + " " + response.message)
                val headers: Headers = response.headers
                for (i in 0 until headers.size) {
                    LogUtils.d("onResponse:" + headers.name(i) + ":" + headers.value(i))
                }
                LogUtils.d("onResponse: " + response.body?.string())
            }
        })
    }

    fun sendVerifyCode(apiUrl: String, key: String, value: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add(key, value)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        LogUtils.d("postForms requestBody = $requestBody")
        LogUtils.d("postForms apiUrl = $apiUrl")
        LogUtils.d("postForms timestamp = " + System.currentTimeMillis())
        val request: Request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        LogUtils.d("request.body = $request.body --- request.url=${request.url}")

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

                if (response.code == 200) {
                    LogUtils.d("发送验证码成功！！！！！！！");
                }
            }
        })
    }

    /**
     * 验证验证码是否正确
     */
    fun checkVerifyCode(apiUrl: String, phoneValue: String, verifyCode: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("phone", phoneValue)
            .add("captcha", verifyCode)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        val request: Request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        LogUtils.d("request.body = $request.body --- request.url=${request.url}")

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

                if (response.code == 200) {
                    LogUtils.d("验证码正确！！！！！！！")
                    //TODO：1、验证手机号码是否注册过，没注册跳注册界面，注册了就跳主界面获取数据
                }
            }
        })
    }

    fun logout() {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ApiConstants.logoutUrl)
            .get() //默认就是GET请求，可以不写
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("onFailure: ")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("onResponse: " + response.body!!.string())
            }
        })
    }

    fun sendRequestWithOneParam(apiUrl: String, key: String, value: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add(key, value)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        LogUtils.d("postForms requestBody = $requestBody")
        LogUtils.d("postForms apiUrl = $apiUrl")
        LogUtils.d("postForms timestamp = " + System.currentTimeMillis())
        val request: Request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        LogUtils.d("request.body = $request.body --- request.url=${request.url}")

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

                if (response.code == 200) {
                    LogUtils.d("发送验证码成功！！！！！！！");
                }
            }
        })
    }
}