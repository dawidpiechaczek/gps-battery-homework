package com.appsirise.piechaczek.gps.homework.view

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.appsirise.piechaczek.gps.homework.GenericDiffCallback
import com.appsirise.piechaczek.gps.homework.databinding.ItemMeasurementBinding

class MeasurementsAdapter : ListAdapter<MeasurementItem, MeasurementViewHolder>(
    GenericDiffCallback<MeasurementItem>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder =
        MeasurementViewHolder(parent, ItemMeasurementBinding::inflate)

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) =
        holder.bind(getItem(position))
}