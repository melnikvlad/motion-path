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
import com.example.motionpath.ui.create_train.adapter.ExerciseDiffCallback.Companion.PAYLOAD_EXERCISE_INDEX

class ExerciseAdapter(
    private val context: Context,
    private val onItemClick: (item: Exercise) -> Unit,
    private val onItemRemoveClick: (item: Exercise) -> Unit,
) : ListAdapter<Exercise, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciseViewHolder(inflater.inflate(R.layout.item_exercise, parent, false))
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
                        if (diff.containsKey(PAYLOAD_EXERCISE_INDEX)) {
                            holder.bindIndex(diff.getInt(PAYLOAD_EXERCISE_INDEX))
                        }
                    }
                    holder.itemView.setOnClickListener { v -> onItemClick(item) }
                    holder.viewClose.setOnClickListener { v -> onItemRemoveClick(item) }
                }
            }
        }
    }

    inner class ExerciseViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val tvExerciseName = v.findViewById<TextView>(R.id.tv_exercise_name)
        private val tvExerciseIndex = v.findViewById<TextView>(R.id.tv_exercise_index)
        val viewClose = v.findViewById<View>(R.id.view_close)

        fun bind(item: Exercise) {
            bindName(item)
            bindIndex(item.index)
        }

        fun bindName(item: Exercise) {
            tvExerciseName.text = item.mockExercise.getLocalizedName(context)
        }

        fun bindIndex(index: Int) {
            tvExerciseIndex.text = index.toString()
        }
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {

    companion object {
        const val PAYLOAD_EXERCISE_INDEX = "exercise_index"
        const val PAYLOAD_EXERCISE_NAME = "exercise_name"
    }

    override fun areItemsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem.mockExercise.id == newItem.mockExercise.id
                && oldItem.index == newItem.index
    }

    override fun areContentsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Exercise, newItem: Exercise): Any? {
        val diff = Bundle()

        if (oldItem.index != newItem.index) {
            diff.putInt(PAYLOAD_EXERCISE_INDEX, newItem.index)
        }

        if (oldItem.mockExercise.name != newItem.mockExercise.name) {
            //diff.putString(PAYLOAD_EXERCISE_NAME, newItem.mockExercise.name)
        }

        if (!diff.isEmpty) return diff

        return super.getChangePayload(oldItem, newItem)
    }
}