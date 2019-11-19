package com.falcotech.mazz.promiselibrary

import android.util.Log
import kotlinx.coroutines.*

object PromiseUtils {
    //newSingleThreadContext("Walrus")
    fun test(promise: Promise<*>){
        CoroutineScope(Dispatchers.Default).launch {
            val result = promise.await()
            Log.d("DEBUG", "result = $result")
        }
    }

    fun <T> ofBg(block: suspend CoroutineScope.() -> T): Promise<T> = CoroutineScope(Dispatchers.Default).async {
        block.invoke(this)
    }

    fun <T> ofUi(block: suspend CoroutineScope.() -> T): Promise<T> = CoroutineScope(Dispatchers.Main).async {
        block.invoke(this)
    }

    fun <T> ofContext(block: suspend CoroutineScope.() -> T, context: CoroutineScope): Promise<T> = context.async {
        block.invoke(this)
    }
}