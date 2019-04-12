package com.falcotech.mazz.promiselibrary

import android.os.Looper
import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

typealias Promise<T> = Deferred<T>

/*
fun <T, Y> Promise<T>.then(handler: (T) -> Y): Promise<Y> = CoroutineScope(Dispatchers.Main)
    .async(Dispatchers.Main, CoroutineStart.LAZY){
        val res = this@then.await()
        handler.invoke(res)
    }
fun <T, Y> Promise<T>.thenAsync(handler: (T) -> Promise<Y>): Promise<Y> = CoroutineScope(Dispatchers.Main)
    .async(Dispatchers.Main, CoroutineStart.LAZY){
        val res = this@thenAsync.await()
        handler.invoke(res).await()
    }
fun <T, Y> Promise<T>.testicals(handler: (T) -> Y) = CoroutineScope(Dispatchers.Main)
    .async(Dispatchers.Main, CoroutineStart.LAZY){
        val res = this@testicals.await()
        val followRes = handler.invoke(res)
        if(followRes is Promise<*>){
            followRes.await()
        }else{
            followRes
        }
    }*/
/*fun <T, Y> Promise<T>.then(handler: (T) -> Y, onMain: Boolean = false) = if(onMain){
    CoroutineScope(Dispatchers.Main)
        .async(this, CoroutineStart.LAZY){

        }
}else{
    CoroutineScope(Dispatchers.Default)
        .async(Dispatchers.Default, CoroutineStart.LAZY){
            val res = this@then.await()
            val followRes = handler.invoke(res)
            if(followRes is Promise<*>){
                followRes.await()
            }else{
                followRes
            }
        }
}*/

/*fun <T, Y> Promise<T>.then(handler: (T) -> Y, onMain: Boolean = false) = if(onMain){
    Log.d("DEBUG", "onMain = $onMain")
    Log.d("DEBUG", "topContext = ${this}")
    val keyMap = this.key
    Log.d("DEBUG", "keyMap = $keyMap")
    CoroutineScope(Dispatchers.Main)
        .async(Dispatchers.Main, CoroutineStart.LAZY){
            Log.d("DEBUG", "inner context = ${this.coroutineContext}")
            val res = this@then.await()
            val followRes = handler.invoke(res)
            if(followRes is Promise<*>){
                followRes.await()
            }else{
                followRes
            }
        }
}else{
    Log.d("DEBUG", "onMain = $onMain")
    Log.d("DEBUG", "topContext = ${this}")
    CoroutineScope(this)
        .async(Dispatchers.Default, CoroutineStart.LAZY){
            Log.d("DEBUG", "inner context = ${this.coroutineContext}")
            val res = this@then.await()
            val followRes = handler.invoke(res)
            if(followRes is Promise<*>){
                followRes.await()
            }else{
                followRes
            }
        }
}*/
/*
fun <T, Y> Promise<T>.then(handler: (T) -> Y, onMain: Boolean = false) = CoroutineScope(this).async(this, CoroutineStart.LAZY){
    val res = this@then.await()
    if(onMain){

    }
}*/
/*fun <T, Y> Promise<T>.then(handler: (T) -> Y, onMain: Boolean = false) = CoroutineScope(Dispatchers.IO)
    .async(Dispatchers.IO, CoroutineStart.LAZY) {
        Log.d("DEBUG", "top context = ${this.coroutineContext}")
        debug()
        val res = this@then.await()
        if (onMain) {
            withContext(Dispatchers.Main) {
                Log.d("DEBUG", "inner context = ${this.coroutineContext}")
                debug()
                val followRes = handler.invoke(res)
                if (followRes is Promise<*>) {
                    followRes.await()
                } else {
                    followRes
                }
            }
        } else {
            val followRes = handler.invoke(res)
            if (followRes is Promise<*>) {
                followRes.await()
            } else {
                followRes
            }
        }
    }*/

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

