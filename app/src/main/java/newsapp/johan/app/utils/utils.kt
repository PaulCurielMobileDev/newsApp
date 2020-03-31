package newsapp.johan.app.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    try {
        val date: Date = format.parse(this)
        System.out.println(date)
        return date
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return Date()
}

fun Date.toStringDate(): String {
    val dateFormat =
        SimpleDateFormat("EEEE MM/dd/yy")
    try {
        return dateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun String.toBeautyDate(): String {
    return this.toDate().toStringDate()
}