package com.appsirise.piechaczek.gps.homework.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.appsirise.piechaczek.gps.homework.extensions.safeOffer
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : LocationRepository {

    override fun getLocation(intervalDelay: Long): Flow<Location> = callbackFlow {
        val permissionFine = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionCoarse = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissionFine == PackageManager.PERMISSION_GRANTED &&
            permissionCoarse == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationServiceImpl", "Permissions granted")
            val request = LocationRequest.create().apply {
                interval = intervalDelay
                fastestInterval = intervalDelay
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val client: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            while (true) {
                client.requestLocationUpdates(request, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        safeOffer(locationResult.lastLocation)
                    }
                }, Looper.getMainLooper())
                delay(intervalDelay)
            }
        }
        awaitClose {
            Log.d("LocationServiceImpl", "Localizator stopped scan")
        }
    }
}