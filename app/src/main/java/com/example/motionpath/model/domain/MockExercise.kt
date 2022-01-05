package com.example.motionpath.model.domain

data class MockExercise(
    val id: Int,
    val categoryId: Int? = null,
    val name: String,
    val isSelected: Boolean = false,
    val exerciseSelectedCount: Int = 0
) {
    fun isCategory() = categoryId != null
}
