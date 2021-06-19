package com.example.motionpath.ui.schedule.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.motionpath.model.domain.BaseSessionUI
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionUI

class SessionsDiffUtilCallback : DiffUtil.ItemCallback<BaseSessionUI>() {
    override fun areItemsTheSame(oldItem: BaseSessionUI, newItem: BaseSessionUI): Boolean {
        return oldItem.session?.id == newItem.session?.id
                && oldItem.session?.time?.start == newItem.session?.time?.start
                && oldItem.session?.time?.finish == newItem.session?.time?.finish
                && oldItem.canShowTime == newItem.canShowTime
    }

    override fun areContentsTheSame(oldItem: BaseSessionUI, newItem: BaseSessionUI): Boolean {
        return oldItem.equals(newItem)
    }
}