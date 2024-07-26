package com.ppx.music.baseinterface

/**
 *
 * @Author Shirley
 * @Date：2024/7/22
 * @Desc：
 */
abstract interface IBase {
    fun setLayoutId() : Int
//
//    fun initData()
//
//    fun initListener()

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    abstract fun getLayoutId(): Int
}