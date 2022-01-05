package com.example.motionpath.di

import android.app.Application
import androidx.room.Room
import com.example.motionpath.data.client.ClientRepositoryImpl
import com.example.motionpath.data.db.*
import com.example.motionpath.data.mock_exercise.MockExerciseRepositoryImpl
import com.example.motionpath.data.train.TrainRepositoryImpl
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.domain.usecase.MockExerciseRepository
import com.example.motionpath.domain.usecase.client.*
import com.example.motionpath.domain.usecase.exercise.GetImpicitMockExercices
import com.example.motionpath.domain.usecase.exercise.GetMockExercisesUseCase
import com.example.motionpath.domain.usecase.exercise.MockExerciseUseCase
import com.example.motionpath.domain.usecase.train.*
import com.example.motionpath.util.DATABASE_NAME
import com.example.motionpath.util.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataBase(app: Application,
                        providerCategoryDao: Provider<CategoryDao>,
                        providerMockExerciseDao: Provider<MockExerciseDao>
    ) = Room
        .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
        .addCallback(
            DataBaseCallback(app.applicationContext, providerCategoryDao, providerMockExerciseDao)
        )
        .build()

    @Provides
    @Singleton
    fun provideDispatcher() = DispatcherProvider(Dispatchers.IO, Dispatchers.Main)

    @Provides
    @Singleton
    fun provideClientDao(db: AppDatabase): ClientDao = db.clientDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun provideMockExerciseDao(db: AppDatabase): MockExerciseDao = db.mockExerciseDao()

    @Provides
    @Singleton
    fun provideClientRepository(db: AppDatabase): ClientRepository = ClientRepositoryImpl(db.clientDao())

    @Provides
    @Singleton
    fun provideTrainRepository(db: AppDatabase): TrainRepository = TrainRepositoryImpl(db.trainDao())

    @Provides
    @Singleton
    fun provideMockExerciseRepository(db: AppDatabase): MockExerciseRepository = MockExerciseRepositoryImpl(db.mockExerciseDao())

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
    fun provideMockExerciseUseCase(repository: MockExerciseRepository) = MockExerciseUseCase(
        GetMockExercisesUseCase(repository),
        GetImpicitMockExercices(repository)
    )
}