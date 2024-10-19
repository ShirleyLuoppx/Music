package com.ppx.music.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MvInfo(
    val id: String,
    val cover: String,
    val songName: String,
    val singerName: String,
    val singerId: String,
    val playCount: Int,
    var playUrl:String
) : Parcelable
