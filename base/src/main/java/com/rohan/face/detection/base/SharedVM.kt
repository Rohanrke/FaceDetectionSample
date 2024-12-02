package com.rohan.face.detection.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedVM : ViewModel() {

    val sharedNavigator = MutableLiveData<NavEvents>()

    val outSideNavigator by lazy {
        MutableLiveData<NavEvents>()
    }

    val activityNavigator by lazy {
        MutableLiveData<NavEvents>()
    }
}