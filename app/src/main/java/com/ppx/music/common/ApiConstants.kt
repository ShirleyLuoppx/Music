package com.ppx.music.common

/**
 *
 * @Author Shirley
 * @Date：2024/7/3
 * @Desc：网络请求路径
 */
class ApiConstants {
    companion object{

        private const val HTTP_URL = "http://119.91.147.152:3000"

        //发送验证码
        const val SEND_VERIFY_CODE = "$HTTP_URL/captcha/sent"

        //验证验证码
        const val CHECK_VERIFY_CODE = "$HTTP_URL/captcha/verify"

        //退出登录
        const val LOGOUT_URL = "$HTTP_URL/logout"

        //检测手机号码是否已注册
        //        /cellphone/existence/check?phone=13xxx
        const val CHECK_PHONE_IS_REGISTED = "$HTTP_URL/cellphone/existence/check"

        //注册(修改密码)
        //      /register/cellphone?phone=13xxx&password=xxxxx&captcha=1234&nickname=binary1345
        const val REGISTER = "$HTTP_URL/register/cellphone"

        //登录状态
        //      /login/status
        const val LOGIN_STATUS = "$HTTP_URL/login/status"

        //获取用户详情
        //      /user/detail?uid=32953014
        const val GET_USER_DETAIL = "$HTTP_URL/user/detail"

        //        根据nickname获取userid
        ///get/userids?nicknames=binaryify /get/userids?nicknames=binaryify;binaryify2
        //可是，如果直接登录，不走注册流程的话，用户的nickName没有接口去获取？  只能直接写入自己的nickName吗，这样不科学.....
        const val GET_USERID_BY_NICKNAME = "$HTTP_URL/get/userids"

        //通过歌曲id获取歌曲的url
        const val GET_URL_BY_SONG_ID = "$HTTP_URL/song/url"

        //通过id获取城市
        const val GET_CITY_BY_ID = "$HTTP_URL/countries/code/list"
    }
}