package com.ppx.music.view.recommend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ppx.music.R
import com.ppx.music.adapter.DailyRecommendPlayListSongsAdapter
import com.ppx.music.databinding.FragmentDailyRecommendPlayListBinding
import com.ppx.music.http.MusicRepository
import com.ppx.music.model.PlayListInfo
import com.ppx.music.player.MusicController
import com.ppx.music.utils.LogUtils
import com.ppx.music.utils.NumberTransUtils
import com.ppx.music.view.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import kotlin.math.roundToInt

/**
 *
 * @Author Shirley
 * @Date：2024/10/14
 * @Desc：每日推荐歌单列表
 */
class DailyRecommendPlayListFragment : BaseFragment<FragmentDailyRecommendPlayListBinding>() {

    private val TAG = "DailyRecommendPlayListFragment"
    private val musicController = MusicController.instance
    private val musicRepository = MusicRepository()
    private var playListInfo: PlayListInfo? = null
    private val dailyRecommendPlayListSongAdapter = DailyRecommendPlayListSongsAdapter()
    private val playListId = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_daily_recommend_play_list
    }

    override fun initView() {
        initRv()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        //歌单信息
        playListInfo = musicController.getPlayListInfo()

        if (playListInfo != null) {

            binding.tvPlayListName.text = playListInfo?.name
            binding.tvPlayCount.text =
                NumberTransUtils.formatNumber(BigDecimal(playListInfo?.playcount.toString()).toPlainString())
//            binding.cvPlaylistInfo.background = Drawable.createFromPath(playListInfo?.picUrl)
            binding.tvPlayListCreatorNickname.text = playListInfo?.creator?.name
            context?.let {
                Glide.with(it).load(playListInfo?.creator?.avatarUrl)
                    .into(binding.ivPlaylistCreatorHeadimg)
            }
            context?.let {
                Glide.with(it).load(playListInfo?.picUrl)
                    .into(binding.ivPlaylistBg)
            }

            val playListId = BigDecimal(playListInfo?.id.toString()).toPlainString()
            Log.d(TAG, "initData: id = $playListId")
            lifecycleScope.launch {
                musicRepository.getPlayListTrackAll(playListId)
            }
        } else {
            LogUtils.d(TAG, "initData: playListInfo is null")
        }
    }

    override fun initListener() {


    }

    override fun onDestroyFragment() {
    }

    private fun getTranslucentColor(percent: Float, rgb: Int): Int {
        val blue = Color.blue(rgb)
        val green = Color.green(rgb)
        val red = Color.red(rgb)
        var alpha = Color.alpha(rgb)
        alpha = (alpha * percent).roundToInt()
        return Color.argb(alpha, red, green, blue)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPaletteColor(url: String) {
        //string类型的在线图片资源 转Bitmap类型的图片资源
        // 创建OkHttpClient实例
        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            // 创建一个Request对象，指定要下载的图片URL
            val request = Request.Builder()
                .url(url) // 替换为实际的图片URL
                .build()

            // 发起请求并获取响应
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // 从响应中获取输入流
                val inputStream: InputStream = response.body!!.byteStream()

                // 使用BitmapFactory将输入流转换为Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                val builder = Palette.from(bitmap)

                builder.generate {
                    val c = it?.getMutedColor(0x000000)
                    Log.d(TAG, "setPaletteColor1: $c")
                    if (c != null) {
                        val color = getTranslucentColor(0.5f, c)
                        Log.d(TAG, "setPaletteColor2: $color")
                        lifecycleScope.launch {
                            binding.cvPlaylistInfo.setBackgroundColor(color)
                        }
                    }
                }
            }
        }


    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvPlaylistSongs.layoutManager = layoutManager
        binding.rvPlaylistSongs.adapter = dailyRecommendPlayListSongAdapter
    }
}