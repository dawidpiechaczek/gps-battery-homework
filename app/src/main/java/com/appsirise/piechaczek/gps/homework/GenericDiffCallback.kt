package com.appsirise.piechaczek.gps.homework

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class GenericDiffCallback<T : UniqueId> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(item1: T, item2: T): Boolean {
        return item1.getUniqueId() == item2.getUniqueId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(item1: T, item2: T): Boolean {
        return item1 == item2
    }
}

interface UniqueId {
    fun getUniqueId(): Any?
}