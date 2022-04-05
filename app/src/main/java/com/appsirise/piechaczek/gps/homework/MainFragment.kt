package com.appsirise.piechaczek.gps.homework

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
import androidx.lifecycle.MediatorLiveData
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

    private var batteryLiveData: LiveData<ViewState<String>> = MutableLiveData()
    private var locationLiveData: LiveData<ViewState<String>> = MutableLiveData()
    private var locationAndBatteryLiveData: LiveData<ViewState<String>> = MutableLiveData()

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
        requestPermissionLauncher?.launch(locationPermission.permissionName)
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
                is ViewState.Error -> Snackbar.make(
                    binding.buttonStop,
                    it.errorMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
                is ViewState.Success -> Snackbar.make(
                    binding.buttonStop,
                    it.data,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startGettingLocation() {
        locationLiveData = mainViewModel.locationLiveData(7000L)
    }

    private fun startGettingBatteryState() {
        batteryLiveData = mainViewModel.batteryLiveData(5000L)
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