package com.ppx.music

import android.content.Intent
import android.os.Build.VERSION_CODES.P
import androidx.core.view.ContentInfoCompat.Flags
import com.alibaba.fastjson.JSON
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.common.SPKey
import com.ppx.music.model.PlaySongUrlEvent
import com.ppx.music.model.ResponseInfo
import com.ppx.music.player.MusicController
import com.ppx.music.player.MusicPlayerService
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.SPUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.create
import okio.IOException
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：okhttp网络请求工具类
 */
class NetRequest {

    companion object {
        val instance: NetRequest by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetRequest()
        }
    }

    fun getSongUrlById(id: String)  {
        val requestBody = FormBody.Builder()
            .add("id", id)
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(ApiConstants.GET_URL_BY_SONG_ID)
            .post(requestBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("getSongUrlById onFailure")
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                LogUtils.d("getSongUrlById onResponse body = $bodyStr")

                val jsonObjectStr = com.alibaba.fastjson.JSONObject.parseObject(bodyStr)
                val dataStr = jsonObjectStr["data"].toString()
                val dataArray = com.alibaba.fastjson.JSONObject.parseArray(dataStr)
                if (dataArray.size > 0) {
                    val data = dataArray[0].toString()
                    val dataObj = com.alibaba.fastjson.JSONObject.parseObject(data)
                    val url = dataObj["url"].toString()
                    LogUtils.d(" NetRequest getSongUrlById url = $url")

                    EventBus.getDefault().post(PlaySongUrlEvent(url))

                } else {
                    LogUtils.d("getSongUrlById dataArray.size = 0")
                }
            }
        })
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
                LogUtils.d("1onResponse: response.protocol = " + response.protocol)
                LogUtils.d("2onResponse:" + (response.code).toString() + " " + response.message)
                val headers: Headers = response.headers
                for (i in 0 until headers.size) {
                    LogUtils.d("3onResponse:" + headers.name(i) + ":" + headers.value(i))
                }
                LogUtils.d("4onResponse: " + response.body?.string())
            }
        })
    }

    //检测手机号码是否已注册
    fun checkPhone(phoneValue: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("phone", phoneValue)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        val request: Request = Request.Builder()
            .url(ApiConstants.CHECK_PHONE_IS_REGISTED)
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

                //注册过
                if (response.code == 200) {
                    LogUtils.d("current phone: $phoneValue is registered")

                    // 发送验证码
                    sendVerifyCode(phoneValue)
                } else if (response.code == 400) {
                    //未注册过
                    LogUtils.d("current phone: $phoneValue is not registered,please register first...")
                    //跳注册界面
                }
            }
        })
    }

    fun sendVerifyCode(value: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("phone", value)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        val request: Request = Request.Builder()
            .url(ApiConstants.SEND_VERIFY_CODE)
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
                    LogUtils.d("发送验证码成功！！！！！！！")
                } else {
                    LogUtils.d("发送验证码失败！")
                }
            }
        })
    }



    /**
     * 根据用户昵称获取用户id
     *    /get/userids?nicknames=binaryify
     */
    fun getUserIdByNickName(nickName: String): Int {
        var userId = -1
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ApiConstants.GET_USERID_BY_NICKNAME + "?nicknames=" + nickName)
            .get() //默认就是GET请求，可以不写
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("getUserIdByNickName onFailure: ")
                iNetCallBack.onFailure(call, e)
            }

            @Throws(java.io.IOException::class)
            override fun onResponse(call: Call, response: Response) {
//                LogUtils.d("getUserIdByNickName onResponse: " + response.body!!.string())

                iNetCallBack.onSuccess(call,response)
            }
        })

        return userId
    }

    /**
     * 获取用户详情
     * /user/detail?uid=494817816
     */
    fun getUserDetail(uid: Int) {

        val request:Request = Request.Builder()
            .url(ApiConstants.GET_USER_DETAIL + "?uid=" + uid)
            .get()
            .build()

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("getUserDetail onFailure: " + e.message)
                iNetCallBack.onFailure(call,e)
            }

            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("getUserDetail onResponse: " + response.body!!.string())
                iNetCallBack.onSuccess(call, response)
            }
        })
    }

    /**
     * 获取登录状态
     */
    fun getLoginStatus(): Boolean {
        var result = false
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        val request: Request = Request.Builder()
            .url(ApiConstants.LOGIN_STATUS)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("onFailure: " + e.message)
                result = false
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("response.protocol = " + response.protocol)
                LogUtils.d((response.code).toString() + " " + response.message)
                LogUtils.d("onResponse: " + response.body!!.string())
                result = true

                //                onResponse: {"data":{"code":200,"account":{"id":9863116836,"userName":"1000_587A66E61231998ECCB52FC5B0A86AFE21B6080C29E31C20C5E7","type":1000,"status":-10,"whitelistAuthority":0,"createTime":1714450278796,"tokenVersion":0,"ban":0,"baoyueVersion":0,"donateVersion":0,"vipType":0,"anonimousUser":true,"paidFee":false},"profile":null}}
//                onResponse: {
//                        "data": {
//                        "code": 200,
//                            "account": {
//                            "id": 9863116836,
//                            "userName": "1000_587A66E61231998ECCB52FC5B0A86AFE21B6080C29E31C20C5E7",
//                            "type": 1000,
//                            "status": -10,
//                            "whitelistAuthority": 0,
//                            "createTime": 1714450278796,
//                            "tokenVersion": 0,
//                            "ban": 0,
//                            "baoyueVersion": 0,
//                            "donateVersion": 0,
//                            "vipType": 0,
//                            "anonimousUser": true,
//                            "paidFee": false
//                        },
//                        "profile": null
//                    }
//                }
            }
        })

        return result
    }

    fun logout() {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(ApiConstants.LOGOUT_URL)
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

    fun sendOkHttpConnection(address: String, vararg params :String,callback: Callback) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(address)
            .build()
        client.newCall(request).enqueue(callback)
    }

    interface INetCallBack {
        fun onFailure(call: Call, e: java.io.IOException)

        fun onSuccess(call: Call, response: Response)
    }

    lateinit var iNetCallBack: INetCallBack
    fun setOnNetCallBack(callBack: INetCallBack){
        iNetCallBack = callBack
    }
}

