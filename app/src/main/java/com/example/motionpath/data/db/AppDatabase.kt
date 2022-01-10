package com.example.motionpath.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.motionpath.R
import com.example.motionpath.data.db.converters.Converters
import com.example.motionpath.di.annotation.ApplicationScope
import com.example.motionpath.model.domain.CategoryType
import com.example.motionpath.model.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    class Callback @Inject constructor(
        private val databaseProvider: Provider<AppDatabase>,
        @ApplicationScope
        private val applicationScope: CoroutineScope,
        private val context: Context
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
                        MockExerciseEntity(id = 1, name = context.getString(R.string.mock_exercise_1)),
                        MockExerciseEntity(id = 2, name = context.getString(R.string.mock_exercise_2)),
                        MockExerciseEntity(id = 3, name = context.getString(R.string.mock_exercise_3)),
                        MockExerciseEntity(id = 4, name = context.getString(R.string.mock_exercise_4)),
                        MockExerciseEntity(id = 5, name = context.getString(R.string.mock_exercise_5)),
                        MockExerciseEntity(id = 6, name = context.getString(R.string.mock_exercise_6)),
                        MockExerciseEntity(id = 7, name = context.getString(R.string.mock_exercise_7)),
                        MockExerciseEntity(id = 8, name = context.getString(R.string.mock_exercise_8)),
                        MockExerciseEntity(id = 9, name = context.getString(R.string.mock_exercise_9)),

                        // Category exercises
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_1)),
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_2)),
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_3)),
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_4)),
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_5)),
                        MockExerciseEntity(parentId = 1, name = context.getString(R.string.mock_exercise_1_6)),
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