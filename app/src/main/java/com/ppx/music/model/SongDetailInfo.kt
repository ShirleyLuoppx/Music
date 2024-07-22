package com.ppx.music.model


//        "dailySongs": [
//            {
//                "name": "异客 (Live版)",
//                "id": 2163982959,
//                "pst": 0,
//                "t": 0,
//                "ar": [
//                    {
//                        "id": 6068,
//                        "name": "杨坤",
//                        "tns": [],
//                        "alias": []
//                    },
//                    {
//                        "id": 52113028,
//                        "name": "王睿卓",
//                        "tns": [],
//                        "alias": []
//                    },
//                    {
//                        "id": 47091532,
//                        "name": "王赫野",
//                        "tns": [],
//                        "alias": []
//                    }
//                ],
//                "alia": [],
//                "pop": 100,
//                "st": 0,
//                "rt": "",
//                "fee": 8,
//                "v": 2,
//                "crbt": null,
//                "cf": "",
//                "al": {
//                    "id": 198583185,
//                    "name": "天赐的声音第五季 第7期",
//                    "picUrl": "http://p1.music.126.net/jWyR23nQNhhyHC_gCmgIPQ==/109951169661028262.jpg",
//                    "tns": [],
//                    "pic_str": "109951169661028262",
//                    "pic": 109951169661028260
//                },
//                "dt": 227250,
//                "h": {
//                    "br": 320000,
//                    "fid": 0,
//                    "size": 9092746,
//                    "vd": -49170,
//                    "sr": 44100
//                },
//                "m": {
//                    "br": 192000,
//                    "fid": 0,
//                    "size": 5455665,
//                    "vd": -46642,
//                    "sr": 44100
//                },
//                "l": {
//                    "br": 128000,
//                    "fid": 0,
//                    "size": 3637124,
//                    "vd": -45113,
//                    "sr": 44100
//                },
//                "sq": {
//                    "br": 980254,
//                    "fid": 0,
//                    "size": 27845359,
//                    "vd": -49524,
//                    "sr": 44100
//                },
//                "hr": null,
//                "a": null,
//                "cd": "01",
//                "no": 8,
//                "rtUrl": null,
//                "ftype": 0,
//                "rtUrls": [],
//                "djId": 0,
//                "copyright": 0,
//                "s_id": 0,
//                "mark": 17179877376,
//                "originCoverType": 2,
//                "originSongSimpleData": {
//                    "songId": 2137785330,
//                    "name": "异客",
//                    "artists": [
//                        {
//                            "id": 0,
//                            "name": "杨坤"
//                        }
//                    ],
//                    "albumMeta": {
//                        "id": 0,
//                        "name": ""
//                    }
//                },
//                "tagPicList": null,
//                "resourceState": true,
//                "version": 2,
//                "songJumpInfo": null,
//                "entertainmentTags": null,
//                "single": 0,
//                "noCopyrightRcmd": null,
//                "rtype": 0,
//                "rurl": null,
//                "mst": 9,
//                "cp": 2712562,
//                "mv": 0,
//                "publishTime": 0,
//                "reason": "昨日十万播放",
//                "videoInfo": {
//                    "moreThanOne": false,
//                    "video": null
//                },
//                "recommendReason": "昨日十万播放",
//                "privilege": {
//                    "id": 2163982959,
//                    "fee": 8,
//                    "payed": 0,
//                    "realPayed": 0,
//                    "st": 0,
//                    "pl": 320000,
//                    "dl": 0,
//                    "sp": 7,
//                    "cp": 1,
//                    "subp": 1,
//                    "cs": false,
//                    "maxbr": 999000,
//                    "fl": 320000,
//                    "pc": null,
//                    "toast": false,
//                    "flag": 2064388,
//                    "paidBigBang": false,
//                    "preSell": false,
//                    "playMaxbr": 999000,
//                    "downloadMaxbr": 999000,
//                    "maxBrLevel": "sky",
//                    "playMaxBrLevel": "sky",
//                    "downloadMaxBrLevel": "sky",
//                    "plLevel": "exhigh",
//                    "dlLevel": "none",
//                    "flLevel": "exhigh",
//                    "rscl": null,
//                    "freeTrialPrivilege": {
//                        "resConsumable": false,
//                        "userConsumable": false,
//                        "listenType": 3,
//                        "cannotListenReason": 1,
//                        "playReason": null
//                    },
//                    "rightSource": 0,
//                    "chargeInfoList": [
//                        {
//                            "rate": 128000,
//                            "chargeUrl": null,
//                            "chargeMessage": null,
//                            "chargeType": 0
//                        },
//                        {
//                            "rate": 192000,
//                            "chargeUrl": null,
//                            "chargeMessage": null,
//                            "chargeType": 0
//                        },
//                        {
//                            "rate": 320000,
//                            "chargeUrl": null,
//                            "chargeMessage": null,
//                            "chargeType": 0
//                        },
//                        {
//                            "rate": 999000,
//                            "chargeUrl": null,
//                            "chargeMessage": null,
//                            "chargeType": 1
//                        }
//                    ]
//                },
//                "alg": "hot_toplist_fill"
//            },

data class SongDetailInfo(
    val songId:Int,
    val songName:String,
    val songArtists:ArrayList<String>,
    val songAlbum:String,
    val picUrl:String
)
