package com.falcotech.mazz.promiselibrary

import kotlinx.coroutines.*

typealias Promise<T> = Deferred<T>

fun <T, Y> Promise<T>.then(handler: (T) -> Y): Promise<Y> = CoroutineScope(Dispatchers.Default)
    .async(Dispatchers.Default, CoroutineStart.LAZY) {
        val res = this@then.await()
        handler.invoke(res)
    }

fun <T, Y> Promise<T>.thenAsync(handler: (T) -> Promise<Y>): Promise<Y> = CoroutineScope(Dispatchers.Default)
    .async(Dispatchers.Default, CoroutineStart.LAZY) {
        val res = this@thenAsync.await()
        handler.invoke(res).await()
    }