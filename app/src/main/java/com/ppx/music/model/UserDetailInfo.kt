package com.ppx.music.model

//getUserDetail onResponse: {
//    "level": 9,
//    "listenSongs": 11863,
//    "userPoint": {
//        "userId": 494817816,
//        "balance": 0,
//        "updateTime": 1721284783993,
//        "version": 10,
//        "status": 0,
//        "blockBalance": 0
//    },
//    "mobileSign": false,
//    "pcSign": false,
//    "profile": {
//        "privacyItemUnlimit": {
//        "area": true,
//        "college": true,
//        "gender": true,
//        "age": true,
//        "villageAge": true
//    },
//        "avatarDetail": null,
//        "avatarImgId": 109951163458530620,
//        "birthday": 631123200000,
//        "gender": 2,
//        "nickname": "oldsportox",
//        "createTime": 1495961634838,
//        "avatarImgIdStr": "109951163458530620",
//        "backgroundImgIdStr": "109951162868126486",
//        "authStatus": 0,
//        "detailDescription": "",
//        "experts": {},
//        "expertTags": null,
//        "userType": 0,
//        "djStatus": 0,
//        "accountStatus": 0,
//        "province": 500000,
//        "city": 500101,
//        "defaultAvatar": false,
//        "avatarUrl": "http://p1.music.126.net/gFEOIU18GBQnydv-XZ5aKA==/109951163458530620.jpg",
//        "backgroundImgId": 109951162868126480,
//        "backgroundUrl": "http://p1.music.126.net/_f8R60U9mZ42sSNvdPn2sQ==/109951162868126486.jpg",
//        "vipType": 0,
//        "mutual": false,
//        "followed": false,
//        "remarkName": null,
//        "description": "",
//        "userId": 494817816,
//        "signature": "321",
//        "authority": 0,
//        "followeds": 8,
//        "follows": 21,
//        "blacklist": false,
//        "eventCount": 0,
//        "allSubscribedCount": 0,
//        "playlistBeSubscribedCount": 0,
//        "followTime": null,
//        "followMe": false,
//        "artistIdentity": [],
//        "cCount": 0,
//        "inBlacklist": false,
//        "sDJPCount": 0,
//        "playlistCount": 7,
//        "sCount": 0,
//        "newFollows": 21
//    },
//    "peopleCanSeeMyPlayRecord": true,
//    "bindings": [{
//        "url": "",
//        "expiresIn": 2147483647,
//        "refreshTime": 1495961627,
//        "bindingTime": 1495961627747,
//        "tokenJsonStr": null,
//        "expired": false,
//        "userId": 494817816,
//        "id": 3128212070,
//        "type": 1
//    }, {
//        "url": "",
//        "expiresIn": 7776000,
//        "refreshTime": 1558402381,
//        "bindingTime": 1495961597884,
//        "tokenJsonStr": null,
//        "expired": true,
//        "userId": 494817816,
//        "id": 3128212071,
//        "type": 5
//    }],
//    "adValid": true,
//    "code": 200,
//    "newUser": false,
//    "recallUser": false,
//    "createTime": 1495961634838,
//    "createDays": 2608,
//    "profileVillageInfo": {
//        "title": "领取村民证",
//        "imageUrl": null,
//        "targetUrl": "https://sg.music.163.com/g/cloud-card-2?nm_style=sbt&market=personal"
//    }
//}
data class UserDetailInfo(
    var userId: Int = -1,
    var nickname: String = "",
    var gender: Int = -1,
    var avatarUrl: String = "",
    var vipType: Int = -1,
    var signature: String = "",
    var followeds: Int = -1,
    var follows: Int = -1,
    var level: Int = 0


)

