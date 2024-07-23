package com.ppx.music.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ppx.music.R

/**
 *
 * @Author Shirley
 * @Date：2024/7/23
 * @Desc：
 */
class TestAFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_find).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.test_frame_layout,TestBFragment())
                ?.commit()

        }
    }
}