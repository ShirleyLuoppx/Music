package com.ppx.music.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.ppx.music.R
import com.ppx.music.model.PlayListInfo

/**
 *
 * @Author Shirley
 * @Date：2024/10/14
 * @Desc：每日推荐歌单adapter
 */
class DailyRecommendPlayListAdapter : BaseQuickAdapter<PlayListInfo, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: PlayListInfo?) {
        Log.d("TAG", "onBindViewHolder: ${item?.playcount.toString()}")
        val tvPlayCount: TextView = holder.getView(R.id.tv_play_count)
        tvPlayCount.text = item?.playcount.toString()
        val tvPlayListName: TextView = holder.getView(R.id.tv_playlist_name)
        tvPlayListName.text = item?.name.toString()
//        holder.setText(R.id.tv_play_count, item?.playcount.toString())
//        holder.setText(R.id.tv_playlist_name, item?.name.toString())
        Glide.with(context).load(item?.picUrl).into(holder.getView(R.id.iv_playlist_bg))
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        // 返回一个 ViewHolder
        return QuickViewHolder(R.layout.item_playlist, parent)
    }
}