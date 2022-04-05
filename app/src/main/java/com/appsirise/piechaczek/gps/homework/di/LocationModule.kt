package com.appsirise.piechaczek.gps.homework.di

import android.content.Context
import com.appsirise.piechaczek.gps.homework.repository.LocationRepository
import com.appsirise.piechaczek.gps.homework.repository.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    fun providesLocationService(@ApplicationContext context: Context): LocationRepository =
        LocationRepositoryImpl(context)
}