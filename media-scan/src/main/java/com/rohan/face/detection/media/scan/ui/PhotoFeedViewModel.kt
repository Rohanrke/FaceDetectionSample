package com.rohan.face.detection.media.scan.ui

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rohan.face.detection.base.BaseNavVM
import com.rohan.face.detection.domain.PhotoFeedRepo
import com.rohan.face.detection.domain.model.FeedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotoFeedViewModel(
    private val repo: PhotoFeedRepo
) : BaseNavVM<PhotoFeedNavEvents>() {

    private val _feedList = MutableStateFlow<List<FeedModel>?>(null)
    val feedList: StateFlow<List<FeedModel>?> get() = _feedList

    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean> get() = _showLoading

    fun fetchAllFeeds() {
        viewModelScope.launch(Dispatchers.IO) {
            _showLoading.postValue(true)
            _feedList.value = repo.getAllFeed()
            _showLoading.postValue(false)
        }
    }

    fun updateNewFeedsFromGallery(list: List<Uri>) {
        viewModelScope.launch(Dispatchers.IO) {
            val feedModelList = mutableListOf<FeedModel>()
            list.forEach {
                feedModelList.add(
                    FeedModel(
                        uri = it,
                        lastUpdatedTimeStamp = System.currentTimeMillis()
                    )
                )
            }
            _feedList.value = repo.insertFeeds(feedModelList)
        }
    }

    fun addNameTag(model: FeedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateFeed(model)
        }
    }

    override fun onNavigatorVMEvent(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        event: PhotoFeedNavEvents
    ) {
        TODO("Not yet implemented")
    }
}