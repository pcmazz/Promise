package com.falcotech.mazz.promiselibrary

import android.util.Log
import kotlinx.coroutines.*

object PromiseUtils {

    fun test(promise: Promise<*>){
        CoroutineScope(Dispatchers.Default).launch {
            val result = promise.await()
            Log.d("DEBUG", "result = $result")
        }
    }

    fun <T> ofBg(block: suspend CoroutineScope.() -> T): Promise<T> = CoroutineScope(newSingleThreadContext("Walrus")).async {
        block.invoke(this)
    }

    fun <T> ofUi(block: suspend CoroutineScope.() -> T): Promise<T> = CoroutineScope(Dispatchers.Main).async {
        block.invoke(this)
    }


}