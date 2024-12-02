package com.rohan.face.detection.base

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface NavEvents
sealed class LoadingEvents : NavEvents {
    data object DismissLoading : LoadingEvents()
}

sealed class AlertDialogEvents : NavEvents {
    class CallBack(var actionCode: Int) : AlertDialogEvents()
}

abstract class BaseNavVM<T : NavEvents> : ViewModel() {

    /**
     * Callback called when @property SharedVM::navigator or SharedVM::activityNavigator is invoked from outside.
     * It is mostly used to communicate from dialogs to activity/fragments to activity .
     * If dialog want to send callback to parent activity, they can use SharedVM::navigator to push events.
     * If fragment want to send callback to parent activity, they can use SharedVM::activityNavigator to push events.
     * In both cases it will invoke this method with corresponding event
     */
    open fun onNavigatorCommonEvent(
        activity: Activity,
        fragmentManager: FragmentManager,
        event: NavEvents
    ) {}

    /**
     * Callback called when we push value to @property navigator.
     * This is created just make every code there in VM and to make testable
     */
    abstract fun onNavigatorVMEvent(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        event: T
    )

    /**
     * Navigator within Fragment which will call ::onNavigatorVMEvent() with corresponding params
     * so that we can navigate using activity or fragmentManager
     */
    val navigator = MutableLiveData<T>()

    /**
     * Navigator to communicate to outside screens which are shown above this fragment such as dialogs
     * For example send LoadingEvents.DismissLoading to dismiss LoadingFragment which is a dialog fragment
     */
    val outSideNavigator by lazy { MutableLiveData<NavEvents>() }

}

