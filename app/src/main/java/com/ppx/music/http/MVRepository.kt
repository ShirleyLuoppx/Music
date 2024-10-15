package com.ppx.music.http

import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.ppx.music.model.MvInfo
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import java.math.BigDecimal

/**
 *
 * @Author Shirley
 * @Date：2024/10/15
 * @Desc：
 */
class MVRepository {
    private val TAG = "MVRepository"
    private val networkService = NetworkService.createService()

    companion object {
        val instance: MVRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MVRepository()
        }
    }

    suspend fun getTopMv() :ArrayList<MvInfo> {
        val str = networkService.getTopMV().toString()
        return analyzeTopMv(str)
//        if (mvList.size > 0) {

//            LogUtils.d(TAG, "getTopMv: mvList id: ${mvList[0].id}")
//            val normalId = BigDecimal(mvList[0].id).toPlainString()
//            LogUtils.d(TAG, "getTopMv: normalId: $normalId")
//            getMVUrlById(normalId)
//        }
    }

    private fun analyzeTopMv(dataStr: String): ArrayList<MvInfo> {
        val mvList = ArrayList<MvInfo>()
        val jo = JSONObject.parseObject(dataStr)
        Log.d(TAG, "getTopMv: $dataStr")
        val code = jo["code"]

        if (code == 200) {
            val dataArrayJo = jo["data"]
            val dataArray = JSONObject.parseArray(dataArrayJo.toString())
            for (data in dataArray) {
                val dataStr = JSONObject.parseObject(data.toString())
                val mvid = dataStr["id"]
                val cover = dataStr["cover"]
                val name = dataStr["name"]
                val playCount = dataStr["playCount"]
                LogUtils.d(TAG, "getTopMv: mvId: $mvid")
                LogUtils.d(TAG, "getTopMv: cover: $cover")
                val mvInfo = MvInfo(mvid.toString(), cover.toString(), name.toString(), playCount.toString().toInt())
                mvList.add(mvInfo)
            }
        } else {
            LogUtils.d(TAG, "getTopMv: failed code: $code")
        }
        return mvList
    }

    suspend fun getMVUrlById(id: String) {
        val str = networkService.getMVUrlById(id).toString()
        analyzeMVUrl(str)
    }

    private fun analyzeMVUrl(dataStr: String): String {
        var mvPlayUrl = ""
        val jo = JSONObject.parseObject(dataStr)
        Log.d(TAG, "getMVUrlById: $dataStr")
        val code = jo["code"]

        if (code == 200) {
            val dataArrayJo = jo["data"]
            val dataJo = JSONObject.parseObject(dataArrayJo.toString())
            val url = dataJo["url"].toString()
            LogUtils.d(TAG, "getMVUrlById: url: $url")

            mvPlayUrl = url
        } else {
            LogUtils.d(TAG, "getMVUrlById: failed code: $code")
        }
        return mvPlayUrl
    }
}