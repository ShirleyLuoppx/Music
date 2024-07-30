package com.ppx.music.model

data class ProvinceInfo(
    val id: Int,
    val name: String,
    val cities: String
)

data class CityInfo(
    val id: String,
    val name: String
)
