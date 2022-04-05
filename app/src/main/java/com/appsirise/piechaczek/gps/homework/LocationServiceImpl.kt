package com.appsirise.piechaczek.gps.homework

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : LocationService {

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

            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    safeOffer(locationResult.lastLocation)
                }
            }, Looper.getMainLooper())
        }
        awaitClose {
            Log.d("LocationServiceImpl", "Localizator stopped scan")
        }
    }
}