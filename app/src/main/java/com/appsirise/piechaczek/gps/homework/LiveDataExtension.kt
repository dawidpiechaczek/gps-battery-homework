package com.appsirise.piechaczek.gps.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A> LiveData<A>.zipWith(stream: LiveData<A>): LiveData<A> {
    val result = MediatorLiveData<A>()
    result.addSource(this) { a ->
        result.value = a
    }
    result.addSource(stream) { b ->
        result.value = b
    }
    return result
}