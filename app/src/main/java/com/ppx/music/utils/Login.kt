package com.ppx.music.utils

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：
 */
class Login {
    var netUtils= NetUtils()

    fun  sendVerifyCodeByPhone(phone:String){
        netUtils.postForms(ApiConstants.loginUrl,"phone",phone)
    }

}