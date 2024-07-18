package com.ppx.music.model

import java.util.Objects

data class ResponseInfo(
    val data:Objects,
    val code: Int,
    val account: LoginStatusInfo,
    val profile: String
)

//                onResponse: {
//                        "data": {
//                        "code": 200,
//                            "account": {
//                            "id": 9863116836,
//                            "userName": "1000_587A66E61231998ECCB52FC5B0A86AFE21B6080C29E31C20C5E7",
//                            "type": 1000,
//                            "status": -10,
//                            "whitelistAuthority": 0,
//                            "createTime": 1714450278796,
//                            "tokenVersion": 0,
//                            "ban": 0,
//                            "baoyueVersion": 0,
//                            "donateVersion": 0,
//                            "vipType": 0,
//                            "anonimousUser": true,
//                            "paidFee": false
//                        },
//                        "profile": null
//                    }
//                }