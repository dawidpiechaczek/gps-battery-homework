package com.appsirise.piechaczek.gps.homework

import android.content.Context
import android.os.BatteryManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(@ApplicationContext applicationContext: Context) :
    BatteryRepository {

    private val batteryManager =
        applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override fun getBatteryState(interval: Long): Flow<Int> = callbackFlow {
        while (true) {
            val batteryLevel =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            safeOffer(batteryLevel)
            delay(interval)
        }
    }
}