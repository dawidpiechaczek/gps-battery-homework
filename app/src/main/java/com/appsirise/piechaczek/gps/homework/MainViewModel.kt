package com.appsirise.piechaczek.gps.homework

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
    private val locationService: LocationService,
    private val batteryRepository: BatteryRepository
) : ViewModel() {

    fun locationLiveData(interval: Long): LiveData<ViewState<String>> =
        liveData(viewModelScope.coroutineContext) {
            locationService.getLocation(interval).collect { location ->
                try {
                    emit(ViewState.Success("Location: ${location.latitude} ${location.longitude}"))
                } catch (error: Throwable) {
                    Log.e("MainViewModel", error.message ?: "Error during getting location")
                    emit(ViewState.Error("Error during getting location"))
                }
            }
        }

    fun batteryLiveData(interval: Long): LiveData<ViewState<String>> =
        liveData(viewModelScope.coroutineContext) {
            batteryRepository.getBatteryState(interval)
                .collect {
                    try {
                        emit(ViewState.Success("Battery level: $it"))
                    } catch (error: Exception) {
                        Log.e(
                            "MainViewModel",
                            error.message ?: "Error during getting battery state"
                        )
                        emit(ViewState.Error("Error during getting battery state"))
                    }
                }
        }
}