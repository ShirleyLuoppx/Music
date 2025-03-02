package com.ppx.music.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.ppx.music.R
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.model.SongVipStatus

/**
 *
 * @Author Shirley
 * @Date：2024/7/23
 * @Desc：推荐页的 每日推荐歌曲 适配器
 */
class DailyRecommendSongAdapter : BaseQuickAdapter<SongDetailInfo, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SongDetailInfo?) {
        // 设置item数据
        val tvSongName : TextView = holder.getView(R.id.tv_song_name)
        val tvSingerName : TextView = holder.getView(R.id.tv_singer_name)
        val tvAlbumName : TextView = holder.getView(R.id.tv_album_name)
        val ivAlbumPic : ImageView = holder.getView(R.id.iv_album_pic)
        val tvIsVipSong:TextView = holder.getView(R.id.tv_is_vip_song)
        val tvIndex:TextView = holder.getView(R.id.tv_index)
        tvSongName.text = item?.songName
        tvAlbumName.text = item?.songAlbum
        tvIsVipSong.visibility = if(item?.songVipStatus == SongVipStatus.FREE || item?.songVipStatus == SongVipStatus.ALL_CAN_PLAY) View.GONE else View.VISIBLE
        tvIndex.visibility = View.GONE
        ivAlbumPic.visibility = View.VISIBLE
        Glide.with(context).load(item?.picUrl).override(50,50).into(ivAlbumPic)

        val singerNamesStr = StringBuilder()
        item?.songArtists?.forEach {
            singerNamesStr.append(it)
            singerNamesStr.append("/")
        }
        tvSingerName.text = singerNamesStr
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        // 返回一个 ViewHolder
        return QuickViewHolder(R.layout.item_daily_recommend, parent)
    }
}