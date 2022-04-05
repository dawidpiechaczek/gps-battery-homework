package com.appsirise.piechaczek.gps.homework

import android.util.Log
import kotlinx.coroutines.channels.SendChannel
import java.util.concurrent.CancellationException

fun <E> SendChannel<E>.safeOffer(value: E) = try {
    trySend(value)
} catch (e: CancellationException) {
    Log.e("LocationServiceImpl", "Safe offer failed with cancellation exception: ${e.message}")
    false
}