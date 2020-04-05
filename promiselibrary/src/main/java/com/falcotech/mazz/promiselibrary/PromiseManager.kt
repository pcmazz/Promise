package com.falcotech.mazz.promiselibrary

import kotlinx.coroutines.CoroutineScope

interface PromiseManager {

    fun <T> async(promise: Promise<T>): Promise<T>

    suspend fun <T> asyncAwait(promise: Promise<T>): T

    suspend fun executeAll(): Any?

    fun cancelAllPromises()

    fun cleanUp()

    fun setDebug(block: (String)->Unit)

    suspend fun <T> launchSingleQueue(block: suspend () -> T): T

    suspend fun <T> cancelThenLaunchControlled(runnerName: String, block: suspend() -> T): T

    suspend fun <T> finishOrLaunchControlled(runnerName: String, block: suspend () -> T): T
}