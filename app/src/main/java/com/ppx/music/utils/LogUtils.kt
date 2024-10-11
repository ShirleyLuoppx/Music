package com.ppx.music.utils

import android.util.Log
import com.ppx.music.common.Constants

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：日志工具类
 */
class LogUtils {
    companion object {
        //TODO:1、添加日志过滤 2、添加各种类型参数打印 3、添加无限数量入参

        fun i(msg: String) {
            Log.i(Constants.TAG, " $msg")
        }

        fun d(msg: String) {
            Log.d(Constants.TAG, " $msg")
        }

        fun e(msg: String) {
            Log.e(Constants.TAG, " $msg")
        }

        fun i(tag: String, msg: String) {
            Log.i(Constants.TAG, "$tag $msg")
        }

        fun d(tag: String, msg: String) {
            Log.d(Constants.TAG, "$tag $msg")
        }

        fun e(tag: String, msg: String) {
            Log.e(Constants.TAG, "$tag $msg")
        }
    }
}