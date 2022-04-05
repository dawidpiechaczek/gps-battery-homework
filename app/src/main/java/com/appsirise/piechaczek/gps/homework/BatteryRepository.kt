package com.appsirise.piechaczek.gps.homework

import kotlinx.coroutines.flow.Flow

interface BatteryRepository {
    fun getBatteryState(interval: Long): Flow<Int>
}