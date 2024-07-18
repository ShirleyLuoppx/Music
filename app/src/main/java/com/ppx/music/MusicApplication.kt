package com.ppx.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/18
 * @Desc：
 */
class MusicApplication : Application() {

    companion object {
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("======================MusicApplication onCreate")
        context = baseContext
        LogUtils.d("======================MusicApplication onCreate context = $context")
    }
}