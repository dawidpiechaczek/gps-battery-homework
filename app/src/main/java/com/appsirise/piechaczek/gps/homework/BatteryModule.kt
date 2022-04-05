package com.appsirise.piechaczek.gps.homework

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BatteryModule {

    @Provides
    fun providesBatteryRepository(@ApplicationContext context: Context): BatteryRepository =
        BatteryRepositoryImpl(context)
}