package com.appsirise.piechaczek.gps.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appsirise.piechaczek.gps.homework.model.AppPermission
import com.appsirise.piechaczek.gps.homework.model.ViewState
import com.appsirise.piechaczek.gps.homework.databinding.FragmentFirstBinding
import com.appsirise.piechaczek.gps.homework.extensions.handlePermissions
import com.appsirise.piechaczek.gps.homework.extensions.zipWith
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// PARAMETERS
const val MAX_LIST_SIZE = 20
const val LOCATION_INTERVAL = 12000L
const val BATTERY_INTERVAL = 5000L

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val locationPermission = AppPermission.AccessFineLocation
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    private var batteryLiveData: LiveData<ViewState<String>> = MutableLiveData()
    private var locationLiveData: LiveData<ViewState<String>> = MutableLiveData()
    private var locationAndBatteryLiveData: LiveData<ViewState<String>> = MutableLiveData()
    private val batteriesAndLocations: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRequestPermissionLauncher()
        setupObservers()
        initView()
        requestPermissionLauncher?.launch(locationPermission.permissionName)
    }

    private fun setupObservers() {
        mainViewModel.uploadedOnServerLiveData.observe(viewLifecycleOwner) { uploadState ->
            when (uploadState) {
                is ViewState.Error -> showSnackbar(uploadState.errorMessage)
                is ViewState.Success -> {
                    batteriesAndLocations.removeAll(uploadState.data)
                    showSnackbar("Measurements sent successfully!")
                }
            }
        }
    }

    private fun initView() {
        binding.buttonStart.setOnClickListener {
            if (locationAndBatteryLiveData.hasActiveObservers()) {
                Toast.makeText(requireContext(), "Ignored", Toast.LENGTH_LONG).show()
            } else {
                startGettingBatteryState()
                startGettingLocation()
                setupLocationAndBatteryObservator()
            }
        }

        binding.buttonStop.setOnClickListener {
            stopGettingBatteryState()
            stopGettingLocation()
            locationAndBatteryLiveData.removeObservers(viewLifecycleOwner)
        }
    }

    private fun setupLocationAndBatteryObservator() {
        locationAndBatteryLiveData = locationLiveData.zipWith(batteryLiveData)
        locationAndBatteryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Error -> showSnackbar(it.errorMessage)
                is ViewState.Success -> {
                    addToListAndCheckSize(it.data)
                    showSnackbar(it.data)
                }
            }
        }
    }


    private fun showSnackbar(message: String) {
        Snackbar.make(binding.buttonStop, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun addToListAndCheckSize(batteryOrLocation: String) {
        batteriesAndLocations.add(batteryOrLocation)
        if (batteriesAndLocations.size >= MAX_LIST_SIZE) {
            mainViewModel.uploadOnServer(batteriesAndLocations)
        }
    }

    private fun startGettingLocation() {
        locationLiveData = mainViewModel.locationLiveData(LOCATION_INTERVAL)
    }

    private fun startGettingBatteryState() {
        batteryLiveData = mainViewModel.batteryLiveData(BATTERY_INTERVAL)
    }

    private fun stopGettingLocation() {
        locationLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun stopGettingBatteryState() {
        batteryLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun setupRequestPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                checkLocationPermission()
            } else {
                Toast.makeText(
                    requireContext(),
                    locationPermission.denialMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkLocationPermission() {
        handlePermissions(
            listOf(locationPermission),
            onAllGranted = {},
            onNotGranted = {
                requestPermissionLauncher?.launch(it.permissionName)
            },
            onRationaleNeeded = {
                Toast.makeText(requireContext(), "Rationale needed", Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onDestroyView() {
        requestPermissionLauncher = null
        super.onDestroyView()
        _binding = null
    }
}