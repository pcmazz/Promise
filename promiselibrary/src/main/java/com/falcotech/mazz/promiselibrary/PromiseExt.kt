package com.falcotech.mazz.promiselibrary

import kotlinx.coroutines.*

typealias Promise<T> = Deferred<T>

fun <T, Y> Promise<T>.then(handler: (T) -> Y) = CoroutineScope(Dispatchers.Default)
    .async(Dispatchers.Default, CoroutineStart.LAZY) {
        val res = this@then.await()
        val followRes = handler.invoke(res)
        if (followRes is Promise<*>) {
            followRes.await()
        } else {
            followRes
        }
    }
