package com.ppx.music.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.ppx.music.MusicApplication

/**
 *
 * @Author Shirley
 * @Date：2024/7/18
 * @Desc：SharedPreferences工具类
 */
class SPUtils {

    companion object {
        val instance: SPUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SPUtils()
        }
    }

    private val sharedPreferences: SharedPreferences = MusicApplication.context.getSharedPreferences("MusicSP", MODE_PRIVATE)

    fun setIntValue(key:String,value:Int){
        sharedPreferences.edit().putInt(key,value).apply()
    }

    fun setStringValue(key:String,value:String){
        sharedPreferences.edit().putString(key,value).apply()
    }

    fun getIntValue(key:String):Int{
        return sharedPreferences.getInt(key,0)
    }

    fun getStringValue(key:String):String{
        return sharedPreferences.getString(key,"")!!
    }
}