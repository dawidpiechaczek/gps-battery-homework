package com.appsirise.piechaczek.gps.homework

import android.location.Location
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
import com.appsirise.piechaczek.gps.homework.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val locationPermission = AppPermission.AccessFineLocation
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    private var batteryLiveData: LiveData<ViewState<Int>> = MutableLiveData()
    private var locationLiveData: LiveData<ViewState<Location>> = MutableLiveData()

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
        initView()

        batteryLiveData.combineWith(locationLiveData) { x, c ->

        }.observe(viewLifecycleOwner) {

        }

        requestPermissionLauncher?.launch(locationPermission.permissionName)
    }

    private fun initView() {
        binding.buttonStart.setOnClickListener {
            if (locationLiveData.hasActiveObservers() || batteryLiveData.hasActiveObservers()) {
                Toast.makeText(requireContext(), "Ignored", Toast.LENGTH_LONG).show()
            } else {
                startGettingBatteryState()
                startGettingLocation()
            }
        }

        binding.buttonStop.setOnClickListener {
            stopGettingBatteryState()
            stopGettingLocation()
        }
    }

    private fun startGettingLocation() {
        locationLiveData = mainViewModel.locationLiveData(10000L)
        locationLiveData.observe(viewLifecycleOwner) { locationState ->
            when (locationState) {
                is ViewState.Error -> Snackbar.make(
                    binding.buttonStop,
                    locationState.errorMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
                is ViewState.Success -> Snackbar.make(
                    binding.buttonStop,
                    "Location: " + locationState.data,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startGettingBatteryState() {
        batteryLiveData = mainViewModel.batteryLiveData(5000L)
        batteryLiveData.observe(viewLifecycleOwner) { batteryState ->
            when (batteryState) {
                is ViewState.Error -> Snackbar.make(
                    binding.buttonStop,
                    batteryState.errorMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
                is ViewState.Success -> Snackbar.make(
                    binding.buttonStop,
                    "Battery: " + batteryState.data,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
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
            onAllGranted = {
                // checkBatteryPermissionsIfNeeded()
            },
            onNotGranted = {
                requestPermissionLauncher?.launch(it.permissionName)
                // checkBatteryPermissionsIfNeeded()
            },
            onRationaleNeeded = { permission ->
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