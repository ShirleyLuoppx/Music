package com.ppx.music.http

/**
 *
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：
 */
data class HttpResponse<out T> (
    val code: Int, val msg: String, val data: T
)