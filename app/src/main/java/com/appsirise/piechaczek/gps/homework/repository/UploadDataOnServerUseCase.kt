package com.appsirise.piechaczek.gps.homework.repository

import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploadDataOnServerUseCase @Inject constructor() {

    suspend fun uploadOnServer(batteriesAndLocations: List<String>): List<String> =
        suspendCoroutine {
            // example api call here
                continuation ->
            continuation.resume(batteriesAndLocations)
        }
}