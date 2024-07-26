package com.ppx.music.view

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.ppx.music.R
import com.ppx.music.databinding.ActivityMainBinding
import com.ppx.music.utils.LogUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var recommendFragment: RecommendFragment
    private lateinit var findFragment: FindFragment
    private lateinit var mineFragment: MineFragment
    private lateinit var tvRecommend: TextView
    private lateinit var tvFind: TextView
    private lateinit var tvMine: TextView

    override fun initView() {
        tvRecommend = binding.include.tvRecommend
        tvFind = binding.include.tvFind
        tvMine = binding.include.tvMine
    }

    override fun initListener() {
        tvRecommend.setOnClickListener { selectFragment(0) }
        tvFind.setOnClickListener { selectFragment(1) }
        tvMine.setOnClickListener { selectFragment(2) }
    }

    override fun initData() {
        initFragment()
        selectFragment(0)
        initListener()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
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

}