package com.example.motionpath.ui.schedule.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionUI

class SessionsDiffUtilCallback : DiffUtil.ItemCallback<SessionUI>() {
    override fun areItemsTheSame(oldItem: SessionUI, newItem: SessionUI): Boolean {
        return oldItem.session?.id == newItem.session?.id
                && oldItem.session?.time?.start == newItem.session?.time?.start
                && oldItem.session?.time?.finish == newItem.session?.time?.finish
    }

    override fun areContentsTheSame(oldItem: SessionUI, newItem: SessionUI): Boolean {
        return oldItem == newItem
    }
}