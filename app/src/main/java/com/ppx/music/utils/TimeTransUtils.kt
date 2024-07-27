package com.ppx.music.utils

/**
 * 时间转换类
 */
object TimeTransUtils {

    private val TAG = "TimeTransUtils"

    /**
     * long类型的时间转换为分钟数
     *
     * time = 206586
     *
     *
     *
     */
    fun long2Minutes(time: Long): String {
        var minuteStr = ""
        val minutes: Int = (time / 1000 / 60).toInt()
        val seconds = time / 1000 % 60
        minuteStr = "$minutes : $seconds"
        LogUtils.d("$TAG long2Minutes minutes = $minuteStr")
        return minuteStr
    }
}