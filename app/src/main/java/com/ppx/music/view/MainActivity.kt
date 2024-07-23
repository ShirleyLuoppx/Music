package com.ppx.music.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.ppx.music.R
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.utils.LogUtils

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var recommendFragment: RecommendFragment
    private lateinit var findFragment: FindFragment
    private lateinit var mineFragment: MineFragment
    private lateinit var tvRecommend: TextView
    private lateinit var tvFind: TextView
    private lateinit var tvMine: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initFragment()
        selectFragment(0)
        initListener()
    }

    private fun initFragment() {
        recommendFragment = RecommendFragment()
        findFragment = FindFragment()
        mineFragment = MineFragment()

//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.framelayout, recommendFragment)
//        fragmentTransaction.add(R.id.framelayout, findFragment)
//        fragmentTransaction.add(R.id.framelayout, mineFragment)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceAsColor")
    private fun selectFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        tvRecommend = binding.include.tvRecommend
        tvFind = binding.include.tvFind
        tvMine = binding.include.tvMine

        when (index) {
            0 -> {

                transaction.replace(R.id.framelayout, recommendFragment)
                LogUtils.d("recommendFragment index =0 .....tvRecommend = $tvRecommend")
                tvRecommend.setTextColor(resources.getColor(R.color.red))
                tvFind.setTextColor(resources.getColor(R.color.black))
                tvMine.setTextColor(resources.getColor(R.color.black))
            }

             1 -> {
                transaction.replace(R.id.framelayout, findFragment)
                tvRecommend.setTextColor(resources.getColor(R.color.black))
                tvFind.setTextColor(resources.getColor(R.color.red))
                tvMine.setTextColor(resources.getColor(R.color.black))
            }

            2 -> {
                transaction.replace(R.id.framelayout, mineFragment)
                tvRecommend.setTextColor(resources.getColor(R.color.black))
                tvFind.setTextColor(resources.getColor(R.color.black))
                tvMine.setTextColor(resources.getColor(R.color.red))
            }
        }

        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        transaction.hide(recommendFragment)
        transaction.hide(findFragment)
        transaction.hide(mineFragment)
    }

    private fun initListener() {
        tvRecommend.setOnClickListener { selectFragment(0) }
        tvFind.setOnClickListener { selectFragment(1) }
        tvMine.setOnClickListener { selectFragment(2) }
    }
}