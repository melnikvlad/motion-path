package com.example.motionpath.ui.clients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R

class ClientsAdapter: ListAdapter<ClientCategory, RecyclerView.ViewHolder>(ClientDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_client_category, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ClientViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class ClientViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvCount: TextView = view.findViewById(R.id.tv_count)
        val tvCactegoryName: TextView = view.findViewById(R.id.tv_category_name)

        fun bind(item: ClientCategory) {
            tvCount.text = item.count.toString()
            tvCactegoryName.text = item.category.name
        }
    }

}

class ClientDiffCallback: DiffUtil.ItemCallback<ClientCategory>() {
    override fun areItemsTheSame(oldItem: ClientCategory, newItem: ClientCategory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ClientCategory, newItem: ClientCategory): Boolean {
        return oldItem == newItem
    }
}