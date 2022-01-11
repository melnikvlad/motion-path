package com.example.motionpath.model.domain.mock_exercise

import android.content.Context
import com.example.motionpath.util.IDENTIFIER_STRING

data class MockExercise(
    val id: Int,
    val categoryId: Int? = null,
    val name: String = "None",
    val muscleName: String? = null,
    val isSelected: Boolean = false,
    val viewType: MockExerciseType = MockExerciseType.ITEM
) {
    var exerciseSelectedCount: Int = 0

    fun getLocalizedName(context: Context): String {
        return context.resources.getString(context.resources.getIdentifier(name, IDENTIFIER_STRING, context.packageName))
    }

    fun getLocalizedMuscleName(context: Context): String {
        return context.resources.getString(context.resources.getIdentifier(muscleName, IDENTIFIER_STRING, context.packageName))
    }
}

enum class MockExerciseType(val code: Int) {
    ITEM(0),
    ITEM_SELECTED(1),

    TITLE_SELECTED(2),
    TITLE_CATEGORY(3),

    ITEM_MUSCLE_NAME(4),
}

enum class MockExerciseItemId(val id: Int) {
    ID_TITLE_SELECTED(10002),
    ID_TITLE_CATEGORY(10003),
    ID_TITLE_MUSCLE_NAME(10004)

}