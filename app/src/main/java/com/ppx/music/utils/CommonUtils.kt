package com.ppx.music.utils

object CommonUtils {

    private var dateArray: ArrayList<String> = ArrayList()
    fun getDate(): ArrayList<String> {
        //获取当前的年/月/日
        val currentDate = java.util.Calendar.getInstance()
        val year = currentDate.get(java.util.Calendar.YEAR)
        val month = currentDate.get(java.util.Calendar.MONTH) + 1
        val day = currentDate.get(java.util.Calendar.DAY_OF_MONTH)
        dateArray.add(year.toString())
        dateArray.add(month.toString())
        dateArray.add(day.toString())
        return dateArray
    }

}