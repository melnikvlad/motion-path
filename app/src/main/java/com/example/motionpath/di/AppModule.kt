package com.example.motionpath.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.motionpath.data.client.ClientRepositoryImpl
import com.example.motionpath.data.db.*
import com.example.motionpath.data.mock_exercise.MockExerciseRepositoryImpl
import com.example.motionpath.data.train.TrainRepositoryImpl
import com.example.motionpath.di.annotation.ApplicationScope
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.domain.usecase.MockExerciseRepository
import com.example.motionpath.domain.usecase.client.*
import com.example.motionpath.domain.usecase.exercise.GetMockExercicesUseCase
import com.example.motionpath.domain.usecase.exercise.GetMockCategoriesUseCase
import com.example.motionpath.domain.usecase.exercise.MockExerciseUseCase
import com.example.motionpath.domain.usecase.train.*
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.domain.ExerciseSelectionRepositoryImpl
import com.example.motionpath.domain.usecase.exercise.GetMockExercisesByQueryUseCase
import com.example.motionpath.util.DATABASE_NAME
import com.example.motionpath.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataBase(app: Application,
                        callback: AppDatabase.Callback
    ) = Room
        .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
        .addCallback(callback)
        .build()

    @Provides
    fun provideApplicationContext(app: Application): Context = app.applicationContext

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideDispatcher() = DispatcherProvider(Dispatchers.IO, Dispatchers.Main)

    @Provides
    fun provideTrainDao(db: AppDatabase): TrainDao = db.trainDao()

    @Provides
    fun provideClientDao(db: AppDatabase): ClientDao = db.clientDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideMockExerciseDao(db: AppDatabase): MockExerciseDao = db.mockExerciseDao()

    @Provides
    @Singleton
    fun provideClientRepository(dao: ClientDao): ClientRepository = ClientRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideTrainRepository(dao: TrainDao): TrainRepository = TrainRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideMockExerciseRepository(dao: MockExerciseDao): MockExerciseRepository = MockExerciseRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideExerciseSelectionRepository(): ExerciseSelectionRepository = ExerciseSelectionRepositoryImpl() //TODO: scope

    @Provides
    @Singleton
    fun provideClientUseCase(repository: ClientRepository) = ClientUseCase(
        GetClientUseCase(repository),
        GetClientsUseCase(repository),
        GetClientsWithTrainsUseCase(repository),
        CreateClientUseCase(repository)
    )

    @Provides
    @Singleton
    fun provideTrainUseCase(repository: TrainRepository) = TrainUseCase(
        CreateTrainUseCase(repository),
        DeleteTrainUseCase(repository),
        GetTrainsForDateUseCase(repository),
        DeleteClientTrainsUseCase(repository),
        GetTrainExercisesUseCase(repository)
    )

    @Provides
    @Singleton
    fun provideMockExerciseUseCase(repository: MockExerciseRepository,
                                   exerciseSelectionRepository: ExerciseSelectionRepository
    ) = MockExerciseUseCase(
        GetMockCategoriesUseCase(repository, exerciseSelectionRepository),
        GetMockExercicesUseCase(repository, exerciseSelectionRepository),
        GetMockExercisesByQueryUseCase(repository, exerciseSelectionRepository)
    )
}