package com.example.motionpath.model.domain.mock_exercise


import com.example.motionpath.model.domain.MockExerciseListItemViewType
import com.example.motionpath.model.domain.UiListItem

data class MockExerciseListItem(val mockExercise: MockExercise) :
    UiListItem<MockExerciseListItemViewType.MockExerciseItem>() {

    override fun itemId(): Int = mockExercise.id

}
