package com.appsirise.piechaczek.gps.homework

sealed class ViewState<out RESULT : Any> {

    data class Success<out RESULT : Any>(val data: RESULT) : ViewState<RESULT>()

    data class Error(val errorMessage: String) : ViewState<Nothing>()

    fun isError(): Boolean = this is Error

    fun isSuccess(): Boolean = this is Success

    fun getSuccessResult(): RESULT? =
        if (this is Success) {
            this.data
        } else {
            null
        }
}