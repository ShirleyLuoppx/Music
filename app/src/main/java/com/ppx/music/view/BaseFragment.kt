package com.ppx.music.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ppx.music.baseinterface.base

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：TODO: 这个没搞清楚咋写的，回去找个GitHub 的demo看看
 */
open class BaseFragment :Fragment(), base {

    private lateinit var binding: androidx.databinding.ViewDataBinding
    private var layoutId:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, setLayoutId(), container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun setLayoutId(): Int {
        return setLayoutId()
    }

    override fun initData() {
    }

    override fun initListener() {
    }
}