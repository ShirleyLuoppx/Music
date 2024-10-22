package com.ppx.music.http

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.ppx.music.MusicApplication
import com.ppx.music.common.Constants
import com.ppx.music.model.PlayListCreatorInfo
import com.ppx.music.model.PlayListInfo
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.model.SongVipStatus
import com.ppx.music.player.MusicController
import com.ppx.music.player.MusicPlayerService
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/10/15
 * @Desc：
 */
class MusicRepository {

    private val TAG = "MusicRepository"
    private val networkService = NetworkService.createService()

    suspend fun getSongUrlById(id: String): String {
        var songUrl = ""
        LogUtils.d(TAG, "getSongUrlById id = $id")

        val newsEntity = networkService.getNewsService(id)

        val jsonObjectStr = JSONObject.parseObject(newsEntity.toString())
        val dataStr = jsonObjectStr["data"].toString()
        val dataArray = JSONObject.parseArray(dataStr)
        if (dataArray.size > 0) {
            val data = dataArray[0].toString()
            val dataObj = JSONObject.parseObject(data)
            val url = dataObj["url"].toString()
            LogUtils.d(TAG, "getSongUrlById url = $url")
            songUrl = url

            startService(url)
        } else {
            LogUtils.d(TAG, "getSongUrlById dataArray.size = 0")
        }
        return songUrl
    }


    private fun startService(url: String) {
        if (!TextUtils.isEmpty(url)) {
            //使用豆包提供的原生的mediaPlayer也可以正常播放...
            val intent = Intent(MusicApplication.context, MusicPlayerService::class.java)
            intent.putExtra("url", url)
            intent.setAction("PLAY")
            MusicApplication.context.startService(intent)
        }
    }

    //获取每日推荐歌单列表
    suspend fun getDailyRecommendPlayList(): ArrayList<PlayListInfo> {
        val playListStr = networkService.getDailyRecommendPlaylist().toString()
        Log.d(TAG, "getDailyRecommendPlayList: $playListStr")

        return analysisPlayList(playListStr)
    }

    //解析每日推荐歌单数据
    private fun analysisPlayList(playListStr: String): ArrayList<PlayListInfo> {
        LogUtils.d(TAG, "analysisPlayList playListStr = $playListStr")

        val playListInfoList = ArrayList<PlayListInfo>()

        if (playListStr.isNotEmpty()) {
            val jsonObject = JSONObject.parseObject(playListStr)
            val bodyCode = jsonObject["code"]
            if (bodyCode == Constants.CODE_SUCCESS) {
                val recommend = jsonObject["recommend"]
                val dailyRecommendPlayListArray = JSONObject.parseArray(recommend.toString())
                playListInfoList.clear()
                for (playList in dailyRecommendPlayListArray) {
                    val jsonObj = JSONObject.parseObject(playList.toString())
                    //歌单信息
                    val playListId = jsonObj["id"].toString().toFloat()
                    val playListName = jsonObj["name"].toString()
                    val playListPicUrl = jsonObj["picUrl"].toString()
                    val playListPlayCount = jsonObj["playcount"].toString()
                    //歌单创建者信息
                    val playListCreatorJO = JSONObject.parseObject(jsonObj["creator"].toString())
                    val playListCreatorName = playListCreatorJO["nickname"].toString()
                    val playListCreatorAvatar = playListCreatorJO["avatarUrl"].toString()

                    val playListInfo =
                        PlayListInfo(
                            playListId, playListName, playListPicUrl, playListPlayCount,
                            PlayListCreatorInfo(playListCreatorName, playListCreatorAvatar)
                        )
                    playListInfoList.add(playListInfo)
                }
                LogUtils.d(
                    TAG,
                    "analysisPlayList success playListInfoList.size = ${playListInfoList.size}"
                )
            } else {
                LogUtils.d(TAG, "analysisPlayList failed code = $bodyCode")
            }
        } else {
            LogUtils.d(TAG, "analysisPlayList playListStr is empty")
        }
        return playListInfoList
    }

    //获取歌单的所有歌曲
    suspend fun getPlayListTrackAll(id: String): ArrayList<SongDetailInfo> {
        val playListTrackAll = networkService.getPlaylistTrackAll(id)
        Log.d(TAG, "getPlayListTrackAll1: $playListTrackAll")

//        val details = networkService.getPlaylistDetail(id)
//        Log.d(TAG, "getPlayListTrackAll2: $details")


        Log.d(TAG, "getPlayListTrackAll: 1111")

        return arrayListOf()
    }

    suspend fun getDailyRecommendSongList(): ArrayList<SongDetailInfo> {
        val data = networkService.getDailyRecommendSongs()
        return analysisDailyRecommendSongListData(data.toString())
    }

    private fun analysisDailyRecommendSongListData(bodyStr: String): ArrayList<SongDetailInfo> {
        val songsInfoList = ArrayList<SongDetailInfo>()
        val bodyJSONObject = JSONObject.parseObject(bodyStr)
        val bodyCode = bodyJSONObject["code"]
        if (bodyCode == Constants.CODE_SUCCESS) {

            val data = bodyJSONObject["data"]
            val dailySongsJO = JSONObject.parseObject(data.toString())
            LogUtils.d("dailySongsJO = $dailySongsJO")

            val dailySongsStr = dailySongsJO["dailySongs"]
            LogUtils.d("onResponse : dailySongsArray = $dailySongsStr")

            val dailySongsArray = JSONObject.parseArray(dailySongsStr.toString())
            LogUtils.d("onResponse : dailySongsArray = ${dailySongsArray.size}")

            for (dailySong in dailySongsArray) {


                val dailySongData = dailySong.toString()
                val jsonObject = JSONObject.parseObject(dailySongData)
                //歌名
                val songName = jsonObject["name"].toString()
//                LogUtils.d("onResponse : songName = $songName")

                //歌曲id
                val songId = jsonObject["id"].toString()
//                LogUtils.d("onResponse : songId = $songId")

                //歌手名
                val ar = jsonObject["ar"].toString()
                val arArray = JSONObject.parseArray(ar)
                val singersList = ArrayList<String>()
                for (arInfo in arArray) {
                    val singleArStr = JSONObject.parseObject(arInfo.toString())
                    val singerName = singleArStr["name"].toString()
                    LogUtils.d("onResponse : when songName = $songName , singerName = $singerName")
                    singersList.add(singerName)
                }

                //歌曲/专辑图片
                val alStr = jsonObject["al"].toString()
                val alObj = JSONObject.parseObject(alStr)
                val picUrl = alObj["picUrl"].toString()
                val alName = alObj["name"].toString()
                //是否是会员歌曲
                val fee = jsonObject["fee"].toString()
                val songVipStatus = initSongVipStatus(fee)
                //歌曲时间 毫秒
                val songTime = jsonObject["dt"].toString().toLong()

                //点赞数量
                //讨论数量

                val songDetailInfo = SongDetailInfo(
                    songId,
                    songName,
                    singersList,
                    alName,
                    picUrl,
                    songVipStatus,
                    songTime
                )
                songsInfoList.add(songDetailInfo)
            }

            MusicController.instance.setMusicDataList(songsInfoList)

        }
        return songsInfoList
    }

    /**
     * feeStr:
     * 0:免费或无版权
     * 1:VIP 歌曲
     * 4:购买专辑
     * 8:非会员可免费播放低音质，会员可播放高音质及下载
     * fee为1或8的歌曲均可单独购买 2元单曲
     */
    private fun initSongVipStatus(feeStr: String): SongVipStatus {
        var vipStatus = SongVipStatus.FREE
        when (feeStr) {
            "0" -> {
                vipStatus = SongVipStatus.FREE
            }

            "1" -> {
                vipStatus = SongVipStatus.VIP
            }

            "4" -> {
                vipStatus = SongVipStatus.BUY_ALBUM
            }

            "8" -> {
                vipStatus = SongVipStatus.ALL_CAN_PLAY
            }
        }
        return vipStatus
    }

    private fun analysisPlayListTrackAll(playListTrackAllStr: String): ArrayList<SongDetailInfo> {
        return arrayListOf()
    }
}