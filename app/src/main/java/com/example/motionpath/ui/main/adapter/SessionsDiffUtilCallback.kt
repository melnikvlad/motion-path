package com.example.motionpath.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.motionpath.model.domain.Session

class SessionsDiffUtilCallback : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
       return oldItem == newItem
    }
}