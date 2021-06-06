package com.example.motionpath.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.domain.Session
import com.example.motionpath.util.getMonthDay
import com.example.motionpath.util.getWeekDay
import com.example.motionpath.util.isToday
import java.util.*

class CalendarAdapter(
    private val context: Context,
    private val screenWidth: Int,
    private val onCalendarDayClick: (CalendarDay) -> Unit
) : PagingDataAdapter<CalendarDay, CalendarAdapter.CalendarDayVH>(DIFF_CALLBACK) {

    private var selectedDate: Date? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarAdapter.CalendarDayVH {
        return CalendarDayVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarDayVH, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                getSelectedItemPos()?.let { pos ->
                    getItem(pos)?.isSelected = false
                }
                onCalendarDayClick.invoke(item)
            }
        }
    }

    inner class CalendarDayVH(view: View) : RecyclerView.ViewHolder(view) {
        private var parentConstraint = view.findViewById<ConstraintLayout>(R.id.parent)
        private var tvWeekDay = view.findViewById<TextView>(R.id.tv_week_day)
        private var tvMonthDay = view.findViewById<TextView>(R.id.tv_month_day)
        private var circle = view.findViewById<RelativeLayout>(R.id.circle_container)


        fun bind(item: CalendarDay) {
            val lp = parentConstraint.layoutParams
            lp.width = screenWidth / 7
            parentConstraint.layoutParams = lp
            tvWeekDay.text = item.date.getWeekDay()
            tvMonthDay.text = item.date.getMonthDay()

            if (item.date == selectedDate) {
                item.isSelected = true
            }

            when {
                item.isSelected -> {
                    circle.background = ContextCompat.getDrawable(context, R.drawable.black_circle)
                    tvMonthDay.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                item.date.isToday() -> {
                    circle.background =
                        ContextCompat.getDrawable(context, R.drawable.black_border_circle)
                    tvMonthDay.setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                else -> {
                    circle.background = null
                    tvMonthDay.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
        }
    }

    fun getSelectedItemPos(): Int? =
        this.snapshot().items.indexOfFirst { it.isSelected }.takeIf { it != -1 }

    fun setSelectedDate(selectedDate: Date) {
        this.selectedDate = selectedDate
    }
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
                && oldItem.isSelected == newItem.isSelected
    }

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }

}