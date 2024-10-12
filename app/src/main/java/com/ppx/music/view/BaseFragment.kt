package com.ppx.music.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

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
}