package com.example.motionpath.ui.create_train.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.domain.train.Train
import com.example.motionpath.model.domain.train.TrainInfoItem
import com.example.motionpath.ui.create_train.adapter.PreviousTrainsDiffCallback.Companion.PAYLOAD_PREVIOUS_TRAIN_SELECTION
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.toStringFormat
import java.lang.StringBuilder

class PreviousTrainsAdapter(
    private val context: Context,
    private val onTranClick: (Train) -> Unit
    ): ListAdapter<Train, RecyclerView.ViewHolder>(PreviousTrainsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PreviousTrainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_previous_train_2, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        getItem(position).let { item ->
            when(holder) {
                is PreviousTrainViewHolder -> {
                    if (payloads.isEmpty() || payloads[0] !is Bundle) {
                        holder.bind(item)
                    } else {
                        val diff = payloads[0] as Bundle
                        if (diff.containsKey(PAYLOAD_PREVIOUS_TRAIN_SELECTION)) {
                            holder.bindSelection(diff.getBoolean(PAYLOAD_PREVIOUS_TRAIN_SELECTION))
                        }
                    }

                    holder.itemView.setOnClickListener { v -> onTranClick(item) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    inner class PreviousTrainViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val parentView: ConstraintLayout = v.findViewById(R.id.parent_view)
        val dateContainer: LinearLayout = v.findViewById(R.id.ll_date)
        val tvDateMonthAndDay: TextView = v.findViewById(R.id.tv_train_date_month_day)
        val tvDateYear: TextView = v.findViewById(R.id.tv_train_date_year)
        val tvExerciseCount: TextView = v.findViewById(R.id.tv_exercise_count)
        val tvAdditional: TextView = v.findViewById(R.id.tv_additional_text)

        fun bind(item: Train) {
            bindText(item)
            bindSelection(item.isSelected)
        }

        fun bindSelection(isSelected: Boolean) {
            if (isSelected) {
                dateContainer.background = ContextCompat.getDrawable(context, R.color.black)
                parentView.background = ContextCompat.getDrawable(context, R.drawable.black_stroke_rectangle_rounded)
            } else {
                dateContainer.background = ContextCompat.getDrawable(context, R.color.default_gray)
                parentView.background = ContextCompat.getDrawable(context, R.drawable.grey_stroke_rectangle_rounded)
            }
        }

        fun bindText(item: Train) {
            tvDateMonthAndDay.text = item.timeStart.toStringFormat(CalendarManager.MONTH_DAY_FORMAT_SHORT)
            tvDateYear.text = item.timeStart.toStringFormat(CalendarManager.YEAR)
            tvExerciseCount.text = String.format(context.getString(R.string.exercises_count_pattern), item.exercises.size)

            val sb = StringBuilder()
            item.exercises
                .distinctBy { it.mockExercise.categoryId }
                .forEach {
                    sb
                        .append(it.mockExercise.getLocalizedCategoryName(context))
                        .append(" / ")
                }

            tvAdditional.text = sb.toString()
        }

    }
}

class PreviousTrainsDiffCallback: DiffUtil.ItemCallback<Train>() {

    companion object {
        const val PAYLOAD_PREVIOUS_TRAIN_SELECTION = "key_previous_train_selected"
    }

    override fun areItemsTheSame(oldItem: Train, newItem: Train): Boolean {
        return oldItem.id == newItem.id
                && oldItem.isSelected == newItem.isSelected
    }

    override fun areContentsTheSame(oldItem: Train, newItem: Train): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Train, newItem: Train): Any? {
        val diff = Bundle()

        if (oldItem.isSelected != newItem.isSelected) {
            diff.putBoolean(PAYLOAD_PREVIOUS_TRAIN_SELECTION, newItem.isSelected)
        }

        if (!diff.isEmpty) return diff

        return super.getChangePayload(oldItem, newItem)
    }
}