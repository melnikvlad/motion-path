package com.example.motionpath.model.domain

@Suppress("UNCHECKED_CAST")
abstract class UiListItem<T : BaseListItemViewType> {

    fun viewType(): BaseListItemViewType = this as T

    fun code(): Int = viewType().code

    abstract fun itemId(): Int
}