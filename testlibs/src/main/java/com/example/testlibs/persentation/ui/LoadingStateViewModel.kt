package com.example.testlibs.persentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface LoadingStateViewModel {
    val loadingState: LiveData<SingleEvent<Boolean>>

    fun showLoading()
    fun hideLoading()
}

open class BaseViewModel : ViewModel(), LoadingStateViewModel {

    private val _loadingState = MutableLiveData<SingleEvent<Boolean>>()
    override val loadingState: LiveData<SingleEvent<Boolean>> = _loadingState

    override fun showLoading() {
        _loadingState.postValue(SingleEvent(true))
    }

    override fun hideLoading() {
        _loadingState.postValue(SingleEvent(false))
    }
}