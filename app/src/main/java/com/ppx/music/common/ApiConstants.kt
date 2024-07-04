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
        val loginUrl = "$preHttpUrl/captcha/sent"
        val logoutUrl = "$preHttpUrl/logout"

        val loginUrlByPhone = "$preHttpUrl/captcha/sent?phone="
    }
}