package com.example.motionpath.ui.exercise.adapter

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
import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseType
import com.example.motionpath.ui.exercise.adapter.ExerciseDiffCallback.Companion.PAYLOAD_COUNT

class ExerciseAdapter(
    private val context: Context,
    private val onItemClick: (item: MockExercise) -> Unit,
    private val onItemRemoveClick: (item: MockExercise) -> Unit,
) : ListAdapter<MockExercise, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MockExerciseType.ITEM.code -> {
                ExerciseViewHolder(inflater.inflate(R.layout.item_mock_exercise, parent, false))
            }

            MockExerciseType.ITEM_SELECTED.code -> {
                SelectedExerciseViewHolder(
                    inflater.inflate(
                        R.layout.item_mock_exercise_selected,
                        parent,
                        false
                    )
                )
            }

            MockExerciseType.TITLE_SELECTED.code -> {
                TitleViewHolder(inflater.inflate(R.layout.item_mock_exercise_title, parent, false))
            }

            MockExerciseType.TITLE_CATEGORY.code -> {
                CategoryTitleViewHolder(
                    inflater.inflate(
                        R.layout.item_mock_exercise_title,
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalStateException("Unknown view type")
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
                is ExerciseViewHolder -> {
                    if (payloads.isEmpty() || payloads[0] !is Bundle) {
                        holder.bind(item)

                    } else {
                        val diff = payloads[0] as Bundle
                        if (diff.containsKey(PAYLOAD_COUNT)) {
                            holder.bindSelection(diff.getInt(PAYLOAD_COUNT))
                        }
                    }
                    holder.itemView.setOnClickListener { v -> onItemClick(item) }
                }

                is TitleViewHolder -> holder.bind()

                is CategoryTitleViewHolder -> holder.bind(item)

                is SelectedExerciseViewHolder -> {
                    holder.bind(item)
                    holder.tvClose.setOnClickListener { v -> onItemRemoveClick(item) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.code
    }

    inner class TitleViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTitle = v.findViewById<TextView>(R.id.tv_title)

        fun bind() {
            tvTitle.text = "Выбрано" // TODO
        }
    }

    inner class CategoryTitleViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTitle = v.findViewById<TextView>(R.id.tv_title)

        fun bind(item: MockExercise) {
            tvTitle.text =
                if (item.name != "None") item.getLocalizedName(context) else context.getString(R.string.title_exercise)
        }
    }

    inner class SelectedExerciseViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val tvName = v.findViewById<TextView>(R.id.tv_name)
        val tvClose = v.findViewById<TextView>(R.id.tv_close)

        fun bind(item: MockExercise) {
            bindName(item)
        }

        private fun bindName(item: MockExercise) {
            tvName.text = item.getLocalizedName(context)
        }
    }

    inner class ExerciseViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val tvName = v.findViewById<TextView>(R.id.tv_name)

        fun bind(item: MockExercise) {
            bindName(item)
            bindSelection(item.exerciseSelectedCount)
        }

        private fun bindName(item: MockExercise) {
            tvName.text = item.getLocalizedName(context)
        }

        fun bindSelection(exerciseSelectedCount: Int) {
            if (exerciseSelectedCount != 0) {
                tvName.text = StringBuilder(tvName.text)
                    .append(" : ")
                    .append(exerciseSelectedCount)
                    .toString()
            }
        }
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<MockExercise>() {

    companion object {
        const val PAYLOAD_COUNT = "exerciseSelectedCount"
    }

    override fun areItemsTheSame(
        oldItem: MockExercise,
        newItem: MockExercise
    ): Boolean {
        return oldItem.id == newItem.id
                && oldItem.exerciseSelectedCount == newItem.exerciseSelectedCount
    }

    override fun areContentsTheSame(
        oldItem: MockExercise,
        newItem: MockExercise
    ): Boolean {
        return oldItem == newItem
                && oldItem.exerciseSelectedCount == newItem.exerciseSelectedCount
    }

    override fun getChangePayload(oldItem: MockExercise, newItem: MockExercise): Any? {
        val diff = Bundle()
        if (oldItem.exerciseSelectedCount != newItem.exerciseSelectedCount) {
            return diff.putInt(PAYLOAD_COUNT, newItem.exerciseSelectedCount)
        }
        return super.getChangePayload(oldItem, newItem)
    }
}