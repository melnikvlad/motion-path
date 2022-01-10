package com.example.motionpath.model.domain.mock_exercise

data class MockExercise(
    val id: Int,
    val categoryId: Int? = null,
    val name: String = "None",
    val isSelected: Boolean = false,
    val viewType: MockExerciseType = MockExerciseType.ITEM
) {
    var exerciseSelectedCount: Int = 0

    fun isCategory() = categoryId == null
}

enum class MockExerciseType(val code: Int) {
    ITEM(0),
    ITEM_SELECTED(1),
    TITLE_SELECTED(2),
    TITLE_CATEGORY(3)
}

enum class MockExerciseItemId(val id: Int) {
    TITLE_SELECTED(10001),
    TITLE_CATEGORY(10002)
}