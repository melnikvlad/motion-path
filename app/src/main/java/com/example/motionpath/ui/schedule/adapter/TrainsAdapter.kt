package com.example.motionpath.ui.schedule.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.domain.getCategoryType
import com.example.motionpath.model.entity.TrainEntity
import com.example.motionpath.model.entity.relations.TrainWithClient
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.extension.setVisible
import com.example.motionpath.util.getDifference
import com.example.motionpath.util.toStringFormat

class TrainsAdapter(
    private val onTrainClick: (TrainWithClient) -> Unit,
    private val onTrainLongClick: (TrainWithClient) -> Unit
) : ListAdapter<TrainWithClient, RecyclerView.ViewHolder>(TrainsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TrainViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        when (holder) {
            is TrainViewHolder -> {
                holder.bind(item)
                holder.itemView.setOnClickListener { onTrainClick(item) }
                holder.itemView.setOnLongClickListener {
                    onTrainLongClick(item)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class TrainViewHolder(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup) : TrainViewHolder {
                return TrainViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false))
            }
        }

        private val constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraint_layout)
        private val tvTrainStart = view.findViewById<TextView>(R.id.tv_train_start)
        private val tvClientName = view.findViewById<TextView>(R.id.tv_client_name)
        private val tvClientGoal = view.findViewById<TextView>(R.id.tv_client_goal)
        private val tvTrainLastLong = view.findViewById<TextView>(R.id.tv_train_last_long)

        fun bind(item: TrainWithClient) {
            bindName(item)
            bindGoal(item)
            bindTimeStart(item)
            bindTrainTimeLast(item)
            bindClientCategory(item)
        }

        private fun bindName(item: TrainWithClient) {
            tvClientName.text = item.client.name
        }

        private fun bindGoal(item: TrainWithClient) {

            tvClientGoal.setVisible(item.client.goal.isNotEmpty())

            tvClientGoal.text = when {
                item.client.goal.isNotEmpty() -> item.client.goal
                item.client.description.isNotEmpty() -> item.client.description
                else -> ""
            }
        }

        private fun bindTimeStart(item: TrainWithClient) {
            tvTrainStart.text = item.train.timeStart.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT)
        }

        private fun bindTrainTimeLast(item: TrainWithClient) {
            val (_, hours, minutes) = getDifference(item.train.timeStart, item.train.timeEnd)
            val sb = StringBuilder()

            if (hours > 0) sb.append(hours).append(" час ")
            if (minutes > 0) sb.append(minutes).append(" мин")

            tvTrainLastLong.text = sb.toString()
        }

        private fun bindClientCategory(item: TrainWithClient) {
            item.client.category.getCategoryType().typeTextColor.run {
                tvClientName.setTextColor(ContextCompat.getColor(context, this))
                tvClientGoal.setTextColor(ContextCompat.getColor(context, this))
                tvTrainLastLong.setTextColor(ContextCompat.getColor(context, this))
            }
        }
    }
}

class TrainsDiffCallback : DiffUtil.ItemCallback<TrainWithClient>() {
    override fun areItemsTheSame(oldItem: TrainWithClient, newItem: TrainWithClient): Boolean {
        return oldItem.train.id == newItem.train.id
    }

    override fun areContentsTheSame(oldItem: TrainWithClient, newItem: TrainWithClient): Boolean {
        return oldItem == newItem
    }
}
