package com.expansion.lg.kimaru.expansion.other

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by kimaru on 4/5/17.
 */

class DisplayDate// public DisplayDate (){}
(internal var epoch: Long?) {
    internal var dateFormatter = SimpleDateFormat("dd/MM/yyyy")
    internal var selectFormatter = SimpleDateFormat("yyyy/M/d")
    internal var timeFormatter = SimpleDateFormat("hh/mm/a")
    internal var dateTimeFormatter = SimpleDateFormat("MM/dd/yyyy hh/mm/a")

    fun dateOnly(): String {
        val date = Date(this.epoch!!)
        return dateFormatter.format(date)
    }

    fun timeOnly(): String {
        val date = Date(this.epoch!!)
        return timeFormatter.format(date)
    }

    fun widgetDateOnly(): String {
        val date = Date(this.epoch!!)
        return selectFormatter.format(date)
    }

    fun dateAndTime(): String {
        val date = Date(this.epoch!!)
        return dateTimeFormatter.format(date)
    }
}
