package com.appsirise.piechaczek.gps.homework.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.appsirise.piechaczek.gps.homework.databinding.ItemMeasurementBinding

class MeasurementViewHolder(
    parent: ViewGroup,
    creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> ItemMeasurementBinding,
) : BaseViewHolder<ItemMeasurementBinding, MeasurementItem>(parent, creator) {

    override fun bind(item: MeasurementItem) {
        with(binding) {
            measurement.text = item.measurement
        }
    }
}