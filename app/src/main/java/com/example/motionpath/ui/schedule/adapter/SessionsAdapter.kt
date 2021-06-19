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
import com.example.motionpath.model.domain.*
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.extension.setVisible
import com.example.motionpath.util.extension.setVisibleOrGone
import com.example.motionpath.util.extension.setVisibleOrHide
import com.example.motionpath.util.toStringFormat


class SessionsAdapter(
    private val context: Context,
    private val onDeleteSession: (SessionUI, View) -> Unit
) : ListAdapter<BaseSessionUI, RecyclerView.ViewHolder>(SessionsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SessionType.CLIENT.code -> SessionViewHolder.from(parent)
            SessionType.FREE.code -> FreeSessionViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown ViewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SessionViewHolder -> {
                val item = getItem(position) as SessionUI
                holder.bind(context, item)
                holder.viewMore.setOnClickListener { v ->
                    onDeleteSession.invoke(item, v)
                }
                holder.itemView.setOnLongClickListener { v ->
                    onDeleteSession.invoke(item, v)
                    return@setOnLongClickListener true
                }
            }
            is FreeSessionViewHolder -> {
                val item = getItem(position) as FreeSessionUI
                holder.bind(context, item)
            }
            else -> throw IllegalArgumentException("Unknown ViewHolder: $holder")
        }
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.code
    }

    fun getFirstClientPos() = currentList.indexOfFirst { it.type == SessionType.CLIENT }
}

class FreeSessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): FreeSessionViewHolder {
            return FreeSessionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_free_session, parent, false))
        }
    }

    private val viewDivider: View = view.findViewById(R.id.view_divider)
    private val tvTime: TextView = view.findViewById(R.id.tv_time)

    fun bind(context: Context, item: FreeSessionUI) {
        viewDivider.setVisibleOrGone(item.canShowTime)
        tvTime.text = item.time?.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT)
    }
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