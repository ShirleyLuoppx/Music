package com.ppx.music.common

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：api接口
 */
class ApiConstants {
    companion object{

        private const val preHttpUrl = "http://119.91.147.152:3000"

        const val sendVerifyCode = "$preHttpUrl/captcha/sent"
        const val checkVerifyCodeUrl = "$preHttpUrl/captcha/verify" //?phone=13xxx&captcha=1597




        const val logoutUrl = "$preHttpUrl/logout"

        const val loginUrlByPhone = "$preHttpUrl/captcha/sent?phone="
    }
}