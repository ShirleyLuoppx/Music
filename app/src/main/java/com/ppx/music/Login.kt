package com.ppx.music

import com.ppx.music.common.ApiConstants
import com.ppx.music.utils.LogUtils

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：
 */
class Login {
    var netUtils= NetRequest()

    fun  sendVerifyCodeByPhone(phone:String){
//        netUtils.postForms(ApiConstants.loginUrl,"phone",phone)
        LogUtils.d("sendVerifyCodeByPhone phone = $phone")
        netUtils.postString(ApiConstants.loginUrlByPhone,phone)
    }

}