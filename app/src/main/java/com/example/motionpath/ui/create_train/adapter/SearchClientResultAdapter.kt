package com.example.motionpath.ui.create_train.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.ui.create_train.Client

class SearchClientResultAdapter(
    private val onClientClick: (Client) -> Unit,
): ListAdapter<Client, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchClientResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_create_train_search_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let { item ->
            when(holder) {
                is SearchClientResultViewHolder -> {
                    holder.bind(item)
                    holder.tvName.setOnClickListener { v -> onClientClick(item) }
                }
            }
        }
    }

    inner class SearchClientResultViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val tvName: TextView = v.findViewById(R.id.tv_name)

        fun bind(item: Client) {
            tvName.text = item.name
        }

    }
}

class DiffCallback: DiffUtil.ItemCallback<Client>() {

    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return oldItem == newItem
    }

}