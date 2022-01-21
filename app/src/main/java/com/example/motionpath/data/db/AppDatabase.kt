package com.example.motionpath.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.motionpath.data.db.converters.Converters
import com.example.motionpath.data.model.entity.*
import com.example.motionpath.di.annotation.ApplicationScope
import com.example.motionpath.model.domain.client_category.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        ClientEntity::class,
        CategoryEntity::class,
        TrainEntity::class,
        ExerciseEntity::class,
        MockExerciseEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun categoryDao(): CategoryDao
    abstract fun trainDao(): TrainDao
    abstract fun mockExerciseDao(): MockExerciseDao
    abstract fun exerciseDao(): ExerciseDao

    class Callback @Inject constructor(
        private val databaseProvider: Provider<AppDatabase>,
        @ApplicationScope
        private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) {
                launch { writeCategories() }
                launch { writeMockedExercises() }
            }
        }

        //TODO: use another aproach
        private suspend fun writeMockedExercises() {
            databaseProvider.get().mockExerciseDao()
                .insert(
                    listOf(
                        // Categories
                        MockExerciseEntity(mockExerciseid = 1, exerciseName = "mock_exercise_1"),
                        MockExerciseEntity(mockExerciseid = 2, exerciseName = "mock_exercise_2"),
                        MockExerciseEntity(mockExerciseid = 3, exerciseName = "mock_exercise_3"),
                        MockExerciseEntity(mockExerciseid = 4, exerciseName = "mock_exercise_4"),
                        MockExerciseEntity(mockExerciseid = 5, exerciseName = "mock_exercise_5"),
                        MockExerciseEntity(mockExerciseid = 6, exerciseName = "mock_exercise_6"),
                        MockExerciseEntity(mockExerciseid = 7, exerciseName = "mock_exercise_7"),
                        MockExerciseEntity(mockExerciseid = 8, exerciseName = "mock_exercise_8"),
                        MockExerciseEntity(mockExerciseid = 9, exerciseName = "mock_exercise_9"),

                        // Category exercises
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_1", exerciseName = "mock_exercise_1_1"),
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_1", exerciseName = "mock_exercise_1_2"),
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_2", exerciseName = "mock_exercise_1_3"),
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_2", exerciseName = "mock_exercise_1_4"),
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_3", exerciseName = "mock_exercise_1_5"),
                        MockExerciseEntity(categoryId = 1, categoryName = "mock_exercise_1", muscleName = "mock_exercise_muscle_1_3", exerciseName = "mock_exercise_1_6"),
                    )
                )
        }

        private suspend fun writeCategories() {
            databaseProvider.get().categoryDao()
                .insert(
                    listOf(
                        CategoryEntity(id = CategoryType.PERSONAL.id, name = CategoryType.PERSONAL.name),
                        CategoryEntity(id = CategoryType.TEMP.id, name = CategoryType.TEMP.name),
                        CategoryEntity(id = CategoryType.DEFAULT.id, name = CategoryType.DEFAULT.name),
                        CategoryEntity(id = CategoryType.OUT.id, name = CategoryType.OUT.name),
                        CategoryEntity(id = CategoryType.PAUSED.id, name = CategoryType.PAUSED.name),
                    )
                )
        }
    }
}