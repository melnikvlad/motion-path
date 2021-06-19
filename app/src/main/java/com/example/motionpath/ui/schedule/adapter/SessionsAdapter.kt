package com.example.motionpath.ui.schedule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionUI
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.extension.setVisible
import com.example.motionpath.util.extension.setVisibleOrHide
import com.example.motionpath.util.toStringFormat


class SessionsAdapter(
    private val context: Context,
    private val onDeleteSession: (SessionUI, View) -> Unit
) : ListAdapter<SessionUI, RecyclerView.ViewHolder>(SessionsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SessionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SessionViewHolder -> {
                val item = getItem(position)
                holder.bind(context, item)
                holder.viewMore.setOnClickListener { v ->
                    onDeleteSession.invoke(item, v)
                }
                holder.itemView.setOnLongClickListener { v ->
                    onDeleteSession.invoke(item, v)
                    return@setOnLongClickListener true
                }
            }
            else -> throw IllegalArgumentException("Unknown ViewHolder: $holder")
        }
    }

    override fun getItemCount() = currentList.size

    fun getFirstClientPos() = currentList.indexOfFirst { !it.isFree() }
}

class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): SessionViewHolder {
            return SessionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false))
        }
    }

    val viewMore: LinearLayout = view.findViewById(R.id.ll_more)
    private val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout)
    private var content: LinearLayout = view.findViewById(R.id.content)
    private var tvTitle: TextView = view.findViewById(R.id.tv_title)
    private var tvSubTitle: TextView = view.findViewById(R.id.tv_subtitle)
    private val tvTimeStart: TextView = view.findViewById(R.id.tv_time_start)
    private val tvTimeEnd: TextView = view.findViewById(R.id.tv_time_end)
    private val viewDividerTop: View = view.findViewById(R.id.view_diveder_top)
    private val viewDividerBottom: View = view.findViewById(R.id.view_diveder_bottom)


    fun bind(context: Context, item: SessionUI) {

//        val constraintSet = ConstraintSet()
//        constraintSet.clone(constraintLayout)
//
//        if (showStartTime) {
//            constraintSet.connect(content.id, ConstraintSet.START, tvTimeStart.id, ConstraintSet.END, 12)
//            constraintSet.connect(content.id, ConstraintSet.TOP, tvTimeStart.id, ConstraintSet.BOTTOM, 12)
//        } else {
//            constraintSet.connect(content.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 54)
//            constraintSet.connect(content.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
//        }
//
//        constraintLayout.setConstraintSet(constraintSet)

        //viewDividerTop.setVisible(showStartTime)
        //tvTimeStart.setVisible(showStartTime)
//        viewDividerBottom.setVisible(showEndTime)
//        tvTimeEnd.setVisible(showEndTime)

        content.setVisibleOrHide(!item.isFree())

        tvTitle.text = String.format(context.getString(R.string.name_pattern), item.session?.client?.name, item.session?.client?.lastName)
        tvSubTitle.text = "${item.session?.client?.getCurrentSessionsCounter()} ${item.session?.client?.comment}"
        tvTimeStart.text = item.session?.time?.start?.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT)
        tvTimeEnd.text = item.session?.time?.finish?.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT)

        tvTimeEnd.setVisibleOrHide(!item.isFree() || item.canShowTime)
        viewDividerBottom.setVisibleOrHide(!item.isFree() || item.canShowTime)
        tvTimeStart.setVisibleOrHide(!item.isFree() || item.canShowTime)
        viewDividerTop.setVisibleOrHide(!item.isFree() || item.canShowTime)
    }
}