package com.appsirise.piechaczek.gps.homework.extensions

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import java.util.concurrent.atomic.AtomicBoolean

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

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(
                "LiveDataExtensions",
                "Multiple observers registered but only one will be notified of changes."
            )
        }
        super.observe(
            owner
        ) { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}
