package com.appsirise.piechaczek.gps.homework

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLocation(): Flow<Location>
}