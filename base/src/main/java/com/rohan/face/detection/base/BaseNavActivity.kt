package com.rohan.face.detection.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

abstract class BaseNavActivity<T : BaseNavVM<U>, U : NavEvents> : AppCompatActivity() {

    protected abstract fun getViewModelFactory(): ViewModelProvider.Factory

    val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        getViewModelUsingFactory()
    }

    abstract fun getViewModelClass(): KClass<T>

    private fun getViewModelUsingFactory(): T =
         ViewModelProvider(this, getViewModelFactory()).get(getViewModelClass().java)

    /**
     * This will be called on onCreate().
     * In this method you set up DI and then access view model to set to binding and call any method in that
     */
    abstract fun onCreateNavAct(savedInstanceState: Bundle?)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateNavAct(savedInstanceState)
        viewModel.navigator
            .observe(this, Observer {
                it?.let {
                    viewModel.onNavigatorVMEvent(this, supportFragmentManager, it)
                }
            })
        val sharedVM = ViewModelProvider(this).get(SharedVM::class.java)
        sharedVM.sharedNavigator
            .observe(this, Observer {
                it?.let {
                    viewModel.onNavigatorCommonEvent(this, supportFragmentManager, it)
                }
            })
        sharedVM.activityNavigator
            .observe(this, Observer {
                it?.let {
                    viewModel.onNavigatorCommonEvent(this, supportFragmentManager, it)
                }
            })
        viewModel.outSideNavigator.observe(this, Observer {
            sharedVM.outSideNavigator.value = it
        })
    }
}