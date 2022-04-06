package com.appsirise.piechaczek.gps.homework.view

import com.appsirise.piechaczek.gps.homework.UniqueId
import java.util.UUID

data class MeasurementItem(
    val uuid: UUID,
    val measurement: String
) : UniqueId {

    override fun getUniqueId() = uuid
}