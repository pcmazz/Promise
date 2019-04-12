package com.falcotech.mazz.promise

import com.falcotech.mazz.promiselibrary.Promise
import com.falcotech.mazz.promiselibrary.PromiseManager
import com.falcotech.mazz.promiselibrary.PromiseUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TestViewModel(promiseManager: PromiseManager): BaseViewModel(promiseManager) {

    fun executePromise(promise: Promise<*>) {
        CoroutineScope(coroutineContext).launch {
            val result = asyncAwait(promise)
        }
    }

    fun testicals(){
        val prom = PromiseUtils.ofBg { "nips" }
        //val result = asyncAwait(prom)
    }
}