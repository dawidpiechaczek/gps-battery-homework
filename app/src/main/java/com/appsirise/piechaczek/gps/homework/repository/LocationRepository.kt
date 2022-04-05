package com.appsirise.piechaczek.gps.homework.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocation(interval: Long): Flow<Location>
}