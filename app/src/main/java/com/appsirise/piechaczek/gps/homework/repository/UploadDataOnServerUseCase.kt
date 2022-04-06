package com.appsirise.piechaczek.gps.homework.repository

import com.appsirise.piechaczek.gps.homework.view.MeasurementItem
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploadDataOnServerUseCase @Inject constructor() {

    suspend fun uploadOnServer(batteriesAndLocations: List<MeasurementItem>): List<MeasurementItem> =
        suspendCoroutine {
            // example api call here
                continuation ->
            continuation.resume(batteriesAndLocations)
        }
}