package com.ppx.music.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.ppx.music.MusicApplication
import com.ppx.music.NetRequest
import com.ppx.music.R
import com.ppx.music.common.ApiConstants
import com.ppx.music.common.Constants
import com.ppx.music.common.SPKey
import com.ppx.music.databinding.ActivityLoginBinding
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.SPUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 *
 * @Author Shirley
 * @Date：2024/7/17
 * @Desc：登录界面
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>(){

    private var netUtils = NetRequest()

    override fun initView() {


    }

    override fun initListener() {
        binding.btnGetVerifyCode.setOnClickListener {
            LogUtils.d("click btn.......")

            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click sendVerifyCode and phoneNumber = $phoneNumber")
            checkPhone(phoneNumber)
        }

        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            LogUtils.d("click btnLogin and phoneNumber = $phoneNumber")

            val verifyCode = binding.etVerifyCode.text.toString()
            LogUtils.d("click btnLogin and verifyCode = $verifyCode")

            checkVerifyCode(phoneNumber, verifyCode)
        }

        binding.btnLogout.setOnClickListener { logout() }
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun destroyView() {

    }

    private fun logout() {
        netUtils.logout()
    }

    //检测手机号码是否已注册
    private fun checkPhone(phoneValue: String) {
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

    fun sendVerifyCode(phoneValue: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("phone", phoneValue)
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
     * 验证验证码是否正确
     */
    private fun checkVerifyCode(phoneValue: String, verifyCode: String) {
        val okHttpClient = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("phone", phoneValue)
            .add("captcha", verifyCode)
            .add("timestamp", System.currentTimeMillis().toString())
            .build()
        val request: Request = Request.Builder()
            .url(ApiConstants.CHECK_VERIFY_CODE)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                LogUtils.d("checkVerifyCode onFailure: " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                LogUtils.d("checkVerifyCode onResponse: " + response.body!!.string())
                if (response.code == 200) {
                    LogUtils.d("验证码正确！！！！！！！")
                    getUserIdByNickName("oldsportox")
                } else {
                    LogUtils.d("验证码错误！")
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
            }

            @Throws(java.io.IOException::class)
            override fun onResponse(call: Call, response: Response) {
//                LogUtils.d("getUserIdByNickName onResponse: " + response.body!!.string())
                val responseBody = response.body
                if (responseBody != null) {

                    //okhttp3请求回调中response.body().string()只能有效调用一次，而我使用了两次，所以在第二次时调用时提示已关闭流的异常
                    val jsonObject = JSONObject(responseBody.string())
                    val code = jsonObject.opt("code")
                    LogUtils.d("getUserIdByNickName onResponse: code = $code")

                    if (code == Constants.CODE_SUCCESS) {
                        val nicknames = jsonObject.opt("nicknames")
                        if (nicknames != null) {
                            val nnJsonObject = JSONObject(nicknames.toString())
                            userId = nnJsonObject.opt("oldsportox")?.toString()?.toInt() ?: -1
                            LogUtils.d("getUserIdByNickName onResponse: userId = $userId")

                            SPUtils.instance.setIntValue(SPKey.USER_ID, userId)
                            SPUtils.instance.setStringValue(SPKey.NICKNAME, "oldsportox")

                            //跳转界面首页
                            val intent =
                                Intent(MusicApplication.context, MainActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                    }
                }
            }
        })

        return userId
    }

}