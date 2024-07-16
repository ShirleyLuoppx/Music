package com.ppx.music.common

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：
 */
class ApiConstants {
    companion object{

        private const val preHttpUrl = "http://119.91.147.152:3000"

        //发送验证码
        val sendVerifyCode = "$preHttpUrl/captcha/sent"
        val loginUrlByPhone = "$preHttpUrl/captcha/sent?phone="
        //验证验证码
        val checkVerifyCode = "$preHttpUrl/captcha/verify"

        val logoutUrl = "$preHttpUrl/logout"
    }
}