package com.example.motionpath.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.CalendarDay
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter : PagingDataAdapter<CalendarDay, CalendarAdapter.CalendarDayVH>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.CalendarDayVH {
        return CalendarDayVH(LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false))
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarDayVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class CalendarDayVH(view: View) : RecyclerView.ViewHolder(view) {
        private var tv = view.findViewById<TextView>(R.id.tv_day)

        fun bind(item: CalendarDay) {
            tv.text = item.date.toStringFormat()
        }
    }
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }

}

fun Date.toStringFormat(stringFormat: String = "dd-MM-yyyy hh:mm:ss"): String {
    val dateFormat = SimpleDateFormat(stringFormat, Locale.getDefault())
    return try {
        dateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        "N/A"
    }
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? {
    val format = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}