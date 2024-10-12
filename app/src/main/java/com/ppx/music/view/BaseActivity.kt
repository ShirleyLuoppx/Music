package com.ppx.music.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ppx.music.databinding.FragmentDailyRecommendBinding

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    abstract fun getLayoutId(): Int
    abstract fun destroyView()

    public lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, getLayoutId())
        setContentView(binding.root)

        setStatusBarTextColor(true)

        initView()
        initListener()
        initData()

    }

    override fun onDestroy() {
        super.onDestroy()

        binding.unbind()
        destroyView()
    }

    fun startActivity(clazz: Class<Any>) {
        val intent = Intent(this, clazz::class.java)
        startActivity(intent)
    }

    //设置顶部状态栏字体颜色，实现沉浸式状态栏
    private fun setStatusBarTextColor(isBlack: Boolean) {
        //这种方式已经被废弃了
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        /*//Android 9及以上版本设置顶部状态栏字体颜色，这种方式相对 ViewCompat.getWindowInsetsController 需要设置参数，但是都能实现一样的效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                APPEARANCE_LIGHT_STATUS_BARS,
                APPEARANCE_LIGHT_STATUS_BARS
            )
        }*/

        //Android 6以及以上都可以用这种方式，去设置状态栏的字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val controller = ViewCompat.getWindowInsetsController(window.decorView)
            controller?.isAppearanceLightStatusBars = isBlack  //true为黑色字体，false为白色字体
        }
    }
}