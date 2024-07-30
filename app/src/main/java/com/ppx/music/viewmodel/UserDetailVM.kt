package com.ppx.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppx.music.model.UserDetailInfo

/**
 *
 * @Author Shirley
 * @Date：2024/7/30
 * @Desc：
 */
class UserDetailVM : ViewModel() {

    private val userDetailInfo = MutableLiveData<UserDetailInfo>()
    private val userInfo:LiveData<UserDetailInfo> get() = userDetailInfo

    fun updateUserDetail(info:UserDetailInfo){
        userDetailInfo.value = info

    }
}