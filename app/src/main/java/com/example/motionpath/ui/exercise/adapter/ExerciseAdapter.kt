package com.example.motionpath.ui.exercise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.domain.MockExercise
import com.example.motionpath.model.entity.MockExerciseEntity

class ExerciseAdapter(
    private val context: Context,
    private val onItemClick: (item: MockExercise) -> Unit,
): ListAdapter<MockExercise, RecyclerView.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mock_exercise, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder is ExerciseViewHolder) {
                holder.bind(item)
                holder.itemView.setOnClickListener { v -> onItemClick(item) }
            }
        }
    }

    inner class ExerciseViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private val tvName = v.findViewById<TextView>(R.id.tv_name)

        fun bind(item: MockExercise) {
            bindName(item.name)
            bindSelection(item.isSelected)
        }

        private fun bindName(name: String) {
            tvName.text = name
        }

        private fun bindSelection(isSelected: Boolean) {
            tvName.setBackgroundColor(
                if (isSelected) ContextCompat.getColor(context, R.color.category_text_color_paused)
                else ContextCompat.getColor(context, R.color.white)
            )
        }
    }
}

class ExerciseDiffCallback: DiffUtil.ItemCallback<MockExercise>() {
    override fun areItemsTheSame(oldItem: MockExercise, newItem: MockExercise) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MockExercise, newItem: MockExercise) = oldItem == newItem
}