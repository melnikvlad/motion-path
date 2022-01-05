package com.example.motionpath.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.motionpath.data.db.converters.Converters
import com.example.motionpath.model.entity.*

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
}