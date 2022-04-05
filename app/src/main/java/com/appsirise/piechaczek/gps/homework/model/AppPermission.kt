package com.appsirise.piechaczek.gps.homework.model

import android.Manifest

sealed class AppPermission(
    val permissionName: String,
    val denialMessage: String,
    val rationaleMessage: String
) {
    object AccessFineLocation : AppPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        "Location permission denied",
        "Location permission denied"
    )

    object Battery : AppPermission(
        Manifest.permission.CAMERA,
        "Location permission denied",
        "Location permission denied"
    )
}