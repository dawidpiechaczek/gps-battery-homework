package com.appsirise.piechaczek.gps.homework

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    locationService: LocationService
) : ViewModel() {

    val locationLiveData: LiveData<ViewState<Location>> = liveData(
        viewModelScope.coroutineContext
    ) {
        locationService.getLocation().collect { location ->
            try {
                emit(ViewState.Success(location))
            } catch (error: Throwable) {
                Log.e("", error.message ?: "Error during getting location")
                emit(ViewState.Error("Error during getting location"))
            }
        }
    }
}