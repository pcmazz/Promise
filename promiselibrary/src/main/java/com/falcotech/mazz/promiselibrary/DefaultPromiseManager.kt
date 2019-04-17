package com.falcotech.mazz.promiselibrary

import androidx.annotation.CallSuper


open class DefaultPromiseManager : PromiseManager{

    private val coldList: ArrayList<Promise<*>> = arrayListOf()
    private var debug = { _: String-> }

    @CallSuper
    @Synchronized
    override fun <T> async(promise: Promise<T>): Promise<T> {
        coldList.add(promise)
        promise.invokeOnCompletion { coldList.remove(promise) }
        return promise
    }

    @CallSuper
    @Synchronized
    override suspend fun <T> asyncAwait(promise: Promise<T>): T {
        return async(promise).await()
    }

    @CallSuper
    @Synchronized
    override suspend fun executeAll(): Any? {
        return recursList(null)
    }

    @CallSuper
    @Synchronized
    override fun cancelAllPromises() {
        if(coldList.isNotEmpty()){
            coldList.forEach {
                it.cancel()
            }
        }
    }

    @CallSuper
    @Synchronized
    override fun cleanUp() {
        cancelAllPromises()
    }

    override fun setDebug(block: (String) -> Unit) {
        debug = block
    }

    private suspend fun recursList(lastRes: Any?): Any?{
        debug(lastRes.toString())
        if(coldList.isEmpty()){
            return lastRes
        }
        return recursList(coldList.last().await())
    }
}