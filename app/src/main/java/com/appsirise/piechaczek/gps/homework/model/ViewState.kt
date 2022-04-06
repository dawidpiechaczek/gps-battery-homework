package com.appsirise.piechaczek.gps.homework.model

sealed class ViewState<out RESULT : Any> {

    data class Success<out RESULT : Any>(val data: RESULT) : ViewState<RESULT>()

    data class Error(val errorMessage: String) : ViewState<Nothing>()

    object Loading : ViewState<Nothing>()

    fun isError(): Boolean = this is Error

    fun isSuccess(): Boolean = this is Success

    fun isLoading(): Boolean = this is Loading

    fun getSuccessResult(): RESULT? =
        if (this is Success) {
            this.data
        } else {
            null
        }
}