package com.appsirise.piechaczek.gps.homework.view

import android.util.Log
import androidx.lifecycle.*
import com.appsirise.piechaczek.gps.homework.extensions.SingleLiveEvent
import com.appsirise.piechaczek.gps.homework.model.ViewState
import com.appsirise.piechaczek.gps.homework.repository.BatteryRepository
import com.appsirise.piechaczek.gps.homework.repository.LocationRepository
import com.appsirise.piechaczek.gps.homework.repository.UploadDataOnServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val batteryRepository: BatteryRepository,
    private val uploadDataOnServerUseCase: UploadDataOnServerUseCase
) : ViewModel() {

    private val _uploadedOnServerLiveData = SingleLiveEvent<ViewState<List<String>>>()
    val uploadedOnServerLiveData: LiveData<ViewState<List<String>>> = _uploadedOnServerLiveData

    fun locationLiveData(interval: Long): LiveData<ViewState<String>> =
        liveData(viewModelScope.coroutineContext) {
            locationRepository.getLocation(interval).collect { location ->
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

    fun uploadOnServer(batteriesAndLocations: List<String>) {
        viewModelScope.launch {
            try {
                val uploadedData = uploadDataOnServerUseCase.uploadOnServer(batteriesAndLocations)
                //simulation of api call with delay
                delay(3000L)
                _uploadedOnServerLiveData.value = ViewState.Success(uploadedData)
            } catch (error: Exception) {
                _uploadedOnServerLiveData.value =
                    ViewState.Error("Error during upload occurred")
            }
        }
    }
}