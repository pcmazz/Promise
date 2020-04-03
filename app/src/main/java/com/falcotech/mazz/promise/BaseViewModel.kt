package com.falcotech.mazz.promise

import androidx.lifecycle.ViewModel
import com.falcotech.mazz.promiselibrary.PromiseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel
constructor(promiseManager: PromiseManager) : ViewModel(), CoroutineScope, PromiseManager by promiseManager{
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job
}