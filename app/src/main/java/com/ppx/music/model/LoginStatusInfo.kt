package com.ppx.music.model

/**
 * 获取登录状态info
 */
data class LoginStatusInfo(
    val id: Int,
    val userName: String,
    val type: Int,
    val status: Int,
    val whitelistAuthority: Int,
    val createTime: Long,
    val tokenVersion: Int,
    val ban: Int,
    val baoyueVersion: Int,
    val donateVersion: Int,
    val vipType: Int,
    val anonimousUser: Boolean,
    val paidFee: Boolean)