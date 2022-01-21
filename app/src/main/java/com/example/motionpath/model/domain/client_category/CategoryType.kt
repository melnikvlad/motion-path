package com.example.motionpath.model.domain.client_category

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.motionpath.R

enum class CategoryType(
    val id: Int,
    @StringRes val typeName: Int,
    @ColorRes val typeBorderColor: Int,
    @ColorRes val typeTextColor: Int,
) {
    PERSONAL(0, R.string.category_name_personal, R.color.category_color_personal, R.color.category_text_color_personal),
    TEMP(1, R.string.category_name_temp, R.color.category_color_temp, R.color.category_text_color_temp),
    DEFAULT(2, R.string.category_name_default, R.color.category_color_default, R.color.category_text_color_default),
    OUT(3, R.string.category_name_out, R.color.category_color_out, R.color.category_text_color_out),
    PAUSED(3, R.string.category_name_paused, R.color.category_color_paused, R.color.category_text_color_paused),
}

fun Int.getCategoryType(): CategoryType {
    return try {
        CategoryType.values().first { it.id == this }
    } catch (e: NoSuchElementException) {
        return CategoryType.DEFAULT
    }
}