package com.ppx.music.adapter

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.ppx.music.R
import com.ppx.music.model.SongDetailInfo
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/23
 * @Desc：
 */
class DailyRecommendAdapter : BaseQuickAdapter<SongDetailInfo, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SongDetailInfo?) {
        // 设置item数据
        val tvSongName : TextView = holder.getView(R.id.tv_song_name)
        val tvSingerName : TextView = holder.getView(R.id.tv_singer_name)
        val tvAlbumName : TextView = holder.getView(R.id.tv_album_name)
        val ivAlbumPic : ImageView = holder.getView(R.id.iv_album_pic)
        tvSongName.text = item?.songName
        tvAlbumName.text = item?.songAlbum
        Glide.with(context).load(item?.picUrl).into(ivAlbumPic)

//        ivAlbumPic.setImageURI(Uri.parse(item?.picUrl))

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