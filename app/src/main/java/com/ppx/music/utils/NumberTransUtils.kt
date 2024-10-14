package com.ppx.music.utils

/**
 * 时间转换类
 */
object NumberTransUtils {

    private val TAG = "TimeTransUtils"

    /**
     * 数字转万或者 亿为单位
     */
    fun formatNumber(number: String): String {
        val num = number.toLongOrNull()?: 0
        return when {
            num < 10000 -> number
            num < 100000000 -> "${num / 10000}万"
            else -> "${num / 100000000 }亿"
        }
    }

    /**
     * long类型的时间转换为分钟数
     * time = 206586 毫秒
     */
    fun long2Minutes(time: Long): String {
        val minutes: Int = (time / 1000 / 60).toInt()
        val seconds = time / 1000 % 60

        val minuteStr = if(minutes<10){
            "0$minutes"
        }else{
            "$minutes"
        }

        val secondStr = if(seconds<10){
            "0$seconds"
        }else{
            "$seconds"
        }

        val timeStr = "$minuteStr:$secondStr"
//        LogUtils.d("$TAG long2Minutes minutes = $timeStr")
        return timeStr
    }
}