package com.falcotech.mazz.promiselibrary

import androidx.annotation.CallSuper


open class DefaultPromiseManager : PromiseManager{

    private val coldList: ArrayList<Promise<*>> = arrayListOf()
    private var singleRunner = SingleRunner()
    private val controlledRunnerMap: MutableMap<String, ControlledRunner<*>> = mutableMapOf()
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
            (coldList.lastIndex downTo 0).forEach {
                coldList[it].cancel()
            }.let {
                coldList.clear()
            }
        }
    }

    @CallSuper
    @Synchronized
    override fun cleanUp() {
        cancelAllPromises()
        singleRunner = SingleRunner()
        if(controlledRunnerMap.isNotEmpty()){
            controlledRunnerMap.forEach {
                it.value.cancel()
            }
            controlledRunnerMap.clear()
        }
    }

    override suspend fun <T> launchSingleQueue(block: suspend () -> T): T {
        return singleRunner.afterPrevious(block)
    }

    override suspend fun <T> cancelThenLaunchControlled(
        runnerName: String,
        block: suspend () -> T
    ): T {
        if(!controlledRunnerMap.containsKey(runnerName)){
            controlledRunnerMap[runnerName] = ControlledRunner<T>()
        }
        return (controlledRunnerMap[runnerName]!! as ControlledRunner<T>).cancelPreviousThenRun(block)
    }

    override suspend fun <T> finishOrLaunchControlled(
        runnerName: String,
        block: suspend () -> T
    ): T {
        if(!controlledRunnerMap.containsKey(runnerName)){
            controlledRunnerMap[runnerName] = ControlledRunner<T>()
        }
        return (controlledRunnerMap[runnerName]!! as ControlledRunner<T>).joinPreviousOrRun(block)
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