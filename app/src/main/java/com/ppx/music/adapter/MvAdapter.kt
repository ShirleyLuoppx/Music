package com.ppx.music.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.ppx.music.R
import com.ppx.music.model.MvInfo
import com.ppx.music.utils.NumberTransUtils
import java.math.BigDecimal

/**
 *
 * @Author Shirley
 * @Date：2024/10/15
 * @Desc：mvadapter
 */
class MvAdapter : BaseQuickAdapter<MvInfo, QuickViewHolder>() {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: MvInfo?) {
        val ivMvBg = holder.getView<ImageView>(R.id.iv_playlist_bg)
        val tvMvName = holder.getView<TextView>(R.id.tv_playlist_name)
        val tvMvPlayCount = holder.getView<TextView>(R.id.tv_play_count)

        Glide.with(context).load(item?.cover).into(ivMvBg)
        tvMvName.text = "${item?.songName} - ${item?.singerName}"
        val scienceStrPlayCount = item?.playCount.toString()
        val playCountStr = BigDecimal(scienceStrPlayCount).toPlainString()
        Log.d("TAG", "onBindViewHolder: $playCountStr")
        tvMvPlayCount.text = NumberTransUtils.formatNumber(playCountStr)
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