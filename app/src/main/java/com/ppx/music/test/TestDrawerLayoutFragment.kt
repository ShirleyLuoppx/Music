package com.ppx.music.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.ppx.music.R

/**
 *
 * @Author Shirley
 * @Date：2024/7/27
 * @Desc：
 */
class TestDrawerLayoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_drawer_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerlayout)

        val tvClick = view.findViewById<TextView>(R.id.tv_click)
        tvClick.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START,true)
        }

    }
}