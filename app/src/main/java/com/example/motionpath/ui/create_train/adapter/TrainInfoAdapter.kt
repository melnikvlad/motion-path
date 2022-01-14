package com.example.motionpath.ui.create_train.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.train.*
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_CLIENT_DESCRIPTION
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_CLIENT_GOAL
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_CLIENT_NAME
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_EXERCISE_INDEX
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_TRAIN_DATE
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_TRAIN_TIME_END
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_TRAIN_TIME_START
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.extension.gone
import com.example.motionpath.util.extension.hide
import com.example.motionpath.util.extension.visible
import com.example.motionpath.util.toStringFormat
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.lang.StringBuilder
import java.util.*

class TrainInfoAdapter(
    private val context: Context,
    private val onSelectExerciseClick: () -> Unit,
    private val onItemRemoveClick: (item: Exercise) -> Unit,
    private val onCollapseOrExpandClick: () -> Unit,
) : ListAdapter<TrainItem, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TrainItem.TRAIN_INFO_ITEM -> {
                TrainInfoViewHolder(
                    inflater.inflate(
                        R.layout.item_train_info,
                        parent,
                        false
                    )
                )
            }

            TrainItem.EXERCISE -> {
                ExerciseViewHolder(inflater.inflate(R.layout.item_exercise, parent, false))
            }

            TrainItem.SELECT_EXERCISE -> {
                SelectExerciseViewHolder(
                    inflater.inflate(
                        R.layout.item_train_select_exercise,
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("$viewType is unknown")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        onBindViewHolder(holder, pos, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        getItem(position)?.let { item ->
            when (holder) {
                is TrainInfoViewHolder -> {
                    if (payloads.isEmpty() || payloads[0] !is Bundle) {
                        holder.bind(item as TrainInfoItem)

                    } else {
                        val diff = payloads[0] as Bundle
                        when {
                            diff.containsKey(PAYLOAD_CLIENT_NAME) -> {
                                diff.getString(PAYLOAD_CLIENT_NAME)?.let {
                                    holder.bindName(it)
                                }
                            }
                            diff.containsKey(PAYLOAD_CLIENT_DESCRIPTION) -> {
                                diff.getString(PAYLOAD_CLIENT_DESCRIPTION)?.let {
                                    holder.bindDescription(it)
                                }
                            }
                            diff.containsKey(PAYLOAD_CLIENT_GOAL) -> {
                                diff.getString(PAYLOAD_CLIENT_GOAL)?.let {
                                    holder.bindGoal(it)
                                }
                            }
                            diff.containsKey(PAYLOAD_TRAIN_DATE) -> {
                                diff.getSerializable(PAYLOAD_TRAIN_DATE)?.let {
                                    holder.bindDate(it as Date)
                                }
                            }
                            diff.containsKey(PAYLOAD_TRAIN_TIME_START) -> {
                                diff.getSerializable(PAYLOAD_TRAIN_TIME_START)?.let {
                                    holder.bindTimeStart(it as Date)
                                }
                            }
                            diff.containsKey(PAYLOAD_TRAIN_TIME_END) -> {
                                diff.getSerializable(PAYLOAD_TRAIN_TIME_END)?.let {
                                    holder.bindTimeEnd(it as Date)
                                }
                            }
                            else -> {
                                holder.bind(item as TrainInfoItem)
                            }
                        }
                    }

                    holder.tvCollapse.setOnClickListener { v -> onCollapseOrExpandClick() }
                }

                is ExerciseViewHolder -> {
                    if (payloads.isEmpty() || payloads[0] !is Bundle) {
                        holder.bind(item as ExerciseItem)

                    } else {
                        val diff = payloads[0] as Bundle
                        if (diff.containsKey(PAYLOAD_EXERCISE_INDEX)) {
                            holder.bindIndex(diff.getInt(PAYLOAD_EXERCISE_INDEX))
                        }
                    }

                    holder.viewClose.setOnClickListener { v ->
                        onItemRemoveClick((item as ExerciseItem).exercise)
                    }
                }

                is SelectExerciseViewHolder -> {
                    holder.itemView.setOnClickListener { v -> onSelectExerciseClick() }
                }

                else -> IllegalArgumentException("$holder is unknown")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.code
    }

    inner class TrainInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewEditName: TextInputEditText = view.findViewById(R.id.input_edit_name)
        private val viewEditDescription: TextInputEditText = view.findViewById(R.id.input_edit_description)
        private val viewEditGoal: TextInputEditText = view.findViewById(R.id.input_edit_goal)

        private val viewEditDate: TextInputEditText = view.findViewById(R.id.input_edit_date)
        private val viewEditTime: TextInputEditText = view.findViewById(R.id.input_edit_time)
        private val viewEditTimeEnd: TextInputEditText = view.findViewById(R.id.input_edit_time_end)

        val tvCollapse: TextView = view.findViewById(R.id.tv_collapse)
        val tvCollapseInfo: TextView = view.findViewById(R.id.tv_collapsed_info)

        fun bind(item: TrainInfoItem) {
            if (!item.isCollapsed) {
                bind(item.client)
                bind(item.trainDate)

                viewEditName.visible()
                viewEditDescription.visible()
                viewEditGoal.visible()
                viewEditDate.visible()
                viewEditTime.visible()
                viewEditTimeEnd.visible()
                tvCollapseInfo.gone()
            } else {
                viewEditName.gone()
                viewEditDescription.gone()
                viewEditGoal.gone()
                viewEditDate.gone()
                viewEditTime.gone()
                viewEditTimeEnd.gone()
                tvCollapseInfo.visible()

                val sb = StringBuilder()
                    .append(item.client.client.name)
                    .append('\n')
                    .append(item.client.client.description)
                    .append('\n')
                    .append(item.client.client.goal)
                    .append('\n')
                    .append("${viewEditDate.text} ${viewEditTime.text} - ${viewEditTimeEnd.text}")

                tvCollapseInfo.text = sb.toString()
            }
        }

        fun bind(item: ClientInfoItem) {
            bindName(item.client.name)
            bindDescription(item.client.description)
            bindGoal(item.client.goal)
        }

        fun bindName(name: String) {
            if (name.isNotEmpty()) {
                viewEditName.setText(name, TextView.BufferType.NORMAL)
            }
        }

        fun bindDescription(description: String) {
            if (description.isNotEmpty()) {
                viewEditDescription.setText(description, TextView.BufferType.NORMAL)
            }
        }

        fun bindGoal(goal: String) {
            if (goal.isNotEmpty()) {
                viewEditGoal.setText(goal, TextView.BufferType.NORMAL)
            }
        }

        fun bind(item: TrainDateInfoItem) {
            bindDate(item.trainDate.day)
            bindTimeStart(item.trainDate.timeStart)
            bindTimeEnd(item.trainDate.timeEnd)
        }

        fun bindDate(day: Date) {
            viewEditDate.setText(
                day.toStringFormat(CalendarManager.DAY_MONTH_WEEKDAY),
                TextView.BufferType.NORMAL
            )
        }

        fun bindTimeStart(timeStart: Date) {
            viewEditTime.setText(
                timeStart.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT),
                TextView.BufferType.NORMAL
            )
        }

        fun bindTimeEnd(timeEnd: Date) {
            viewEditTimeEnd.setText(
                timeEnd.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT),
                TextView.BufferType.NORMAL
            )
        }
    }

    inner class ExerciseViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val tvExerciseName = v.findViewById<TextView>(R.id.tv_exercise_name)
        private val tvExerciseIndex = v.findViewById<TextView>(R.id.tv_exercise_index)
        val viewClose = v.findViewById<View>(R.id.view_close)

        fun bind(item: ExerciseItem) {
            bindName(item.exercise)
            bindIndex(item.exercise.index)
        }

        fun bindName(item: Exercise) {
            tvExerciseName.text = item.mockExercise.getLocalizedName(context)
        }

        fun bindIndex(index: Int) {
            tvExerciseIndex.text = index.toString()
        }
    }

    inner class SelectExerciseViewHolder(v: View) : RecyclerView.ViewHolder(v)
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<TrainItem>() {

    companion object {
        const val PAYLOAD_EXERCISE_INDEX = "exercise_index"
        const val PAYLOAD_EXERCISE_NAME = "exercise_name"

        const val PAYLOAD_CLIENT_NAME = "client_name"
        const val PAYLOAD_CLIENT_GOAL = "client_goal"
        const val PAYLOAD_CLIENT_DESCRIPTION = "client_description"

        const val PAYLOAD_TRAIN_DATE = "train_date"
        const val PAYLOAD_TRAIN_TIME_START = "train_time_start"
        const val PAYLOAD_TRAIN_TIME_END = "train_time_end"
    }

    override fun areItemsTheSame(oldItem: TrainItem, newItem: TrainItem): Boolean {
        return when (oldItem) {
            is TrainInfoItem -> {
                if (newItem is TrainInfoItem) {
                    oldItem.client.client == newItem.client.client
                            && oldItem.trainDate.trainDate == newItem.trainDate.trainDate
                            && oldItem.isCollapsed == newItem.isCollapsed
                } else false

            }
            is ExerciseItem -> {
                if (newItem is ExerciseItem) {
                    oldItem.exercise.mockExercise.id == newItem.exercise.mockExercise.id
                            && oldItem.exercise.index == newItem.exercise.index
                } else false
            }

            is SelectExerciseItem -> newItem is SelectExerciseItem

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: TrainItem, newItem: TrainItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: TrainItem, newItem: TrainItem): Any? {
        val diff = Bundle()

        if (oldItem is ExerciseItem && newItem is ExerciseItem) {
            if (oldItem.exercise.index != newItem.exercise.index) {
                diff.putInt(PAYLOAD_EXERCISE_INDEX, newItem.exercise.index)
            }
        }

        if (oldItem is TrainInfoItem && newItem is TrainInfoItem) {
            if (oldItem.client.client.name != newItem.client.client.name) {
                diff.putString(PAYLOAD_CLIENT_NAME, newItem.client.client.name)
            }
            if (oldItem.client.client.description != newItem.client.client.description) {
                diff.putString(PAYLOAD_CLIENT_DESCRIPTION, newItem.client.client.description)
            }
            if (oldItem.client.client.goal != newItem.client.client.goal) {
                diff.putString(PAYLOAD_CLIENT_GOAL, newItem.client.client.goal)
            }

            if (oldItem.trainDate.trainDate.day != newItem.trainDate.trainDate.day) {
                diff.putSerializable(PAYLOAD_TRAIN_DATE, newItem.trainDate.trainDate.day)
            }
            if (oldItem.trainDate.trainDate.timeStart != newItem.trainDate.trainDate.timeStart) {
                diff.putSerializable(
                    PAYLOAD_TRAIN_TIME_START,
                    newItem.trainDate.trainDate.timeStart
                )
            }
            if (oldItem.trainDate.trainDate.timeEnd != newItem.trainDate.trainDate.timeEnd) {
                diff.putSerializable(PAYLOAD_TRAIN_TIME_END, newItem.trainDate.trainDate.timeEnd)
            }
        }

        if (!diff.isEmpty) return diff

        return super.getChangePayload(oldItem, newItem)
    }
}