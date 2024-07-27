package com.ppx.music.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ppx.music.databinding.FragmentDailyRecommendBinding

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    abstract fun getLayoutId(): Int

    public lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this,getLayoutId())
        setContentView(binding.root)

        initView()
        initListener()
        initData()

    }

    override fun onDestroy() {
        super.onDestroy()

        binding.unbind()
    }

    fun startActivity(clazz: Class<Any>){
        val intent = Intent(this,clazz::class.java)
        startActivity(intent)
    }
}