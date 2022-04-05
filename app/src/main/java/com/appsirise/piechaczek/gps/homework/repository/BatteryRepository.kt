package com.appsirise.piechaczek.gps.homework.repository

import kotlinx.coroutines.flow.Flow

interface BatteryRepository {
    fun getBatteryState(interval: Long): Flow<Int>
}