package com.ppx.music.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ppx.music.R

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：
 */
abstract class BaseFragment<V:ViewDataBinding> : Fragment() {

    lateinit var binding: V
    private var layoutId:Int = 0

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
    abstract fun initListener()
    abstract fun onDestroyFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        setStatusBarTextColor(true,binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.unbind()

        onDestroyFragment()
    }

    fun replaceFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.framelayout, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    //设置顶部状态栏字体颜色，实现沉浸式状态栏
    private fun setStatusBarTextColor(isBlack: Boolean,view: View) {
        //Android 6以及以上都可以用这种方式，去设置状态栏的字体颜色，更推荐这种方式，使用更简单，兼容性更好，安全性也更高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val controller = ViewCompat.getWindowInsetsController(view)
            controller?.isAppearanceLightStatusBars = isBlack  //true为黑色字体，false为白色字体
        }
    }

}