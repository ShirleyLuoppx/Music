package com.ppx.music.common

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：
 */
class ApiConstants {
    companion object{

        private const val preHttpUrl = "http://119.91.147.152:3000"

        //发送验证码
        val sendVerifyCode = "$preHttpUrl/captcha/sent"

        val loginUrlByPhone = "$preHttpUrl/captcha/sent?phone="

        //验证验证码
        val checkVerifyCode = "$preHttpUrl/captcha/verify"

        //退出登录
        val logoutUrl = "$preHttpUrl/logout"

        //检测手机号码是否已注册
        //        /cellphone/existence/check?phone=13xxx
        val checkPhoneIsRegisted = "$preHttpUrl/cellphone/existence/check"

        //注册(修改密码)
        //      /register/cellphone?phone=13xxx&password=xxxxx&captcha=1234&nickname=binary1345
        val register = "$preHttpUrl/register/cellphone"

        //登录状态
        //      /login/status
        val loginStatus = "$preHttpUrl/login/status"

        //获取用户详情
        //      /user/detail?uid=32953014
        val getUserDetail = "$preHttpUrl/user/detail"

        //        根据nickname获取userid
        ///get/userids?nicknames=binaryify /get/userids?nicknames=binaryify;binaryify2
        //可是，如果直接登录，不走注册流程的话，用户的nickName没有接口去获取？  只能直接写入自己的nickName吗，这样不科学.....
        val getUserIdByNickName = "$preHttpUrl/get/userids"

    }
}