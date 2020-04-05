package com.falcotech.mazz.promiselibrary

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

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


class SingleRunner{

    private val mutex = Mutex()

    suspend fun <T> afterPrevious(block: suspend () -> T): T {
        mutex.withLock {
            return block()
        }
    }

    suspend fun <T> afterPrevious(block: suspend () -> T, owner: Any): T {
        mutex.withLock(owner) {
            return block()
        }
    }
}

class ControlledRunner<T>{

    private val activeTask = AtomicReference<Deferred<T>?>(null)

    suspend fun cancelPreviousThenRun(block: suspend() -> T): T {
        activeTask.get()?.cancelAndJoin()

        return coroutineScope {
            val newTask = async(start = CoroutineStart.LAZY) {
                block()
            }

            newTask.invokeOnCompletion {
                activeTask.compareAndSet(newTask, null)
            }

            val result: T

            while(true){
                if(!activeTask.compareAndSet(null, newTask)){
                    activeTask.get()?.cancelAndJoin()
                    yield()
                }else{
                    result = newTask.await()
                    break
                }
            }
            result
        }
    }
    //handler: (T) -> Y
    suspend fun joinPreviousOrRun(block: suspend () -> T): T {
        activeTask.get()?.let {
            return it.await()
        }
        return coroutineScope {
            val newTask = async(start = CoroutineStart.LAZY) {
                block()
            }

            newTask.invokeOnCompletion {
                activeTask.compareAndSet(newTask, null)
            }

            val result: T

            while(true){
                if(!activeTask.compareAndSet(null, newTask)){
                    val currentTask = activeTask.get()
                    if(currentTask != null){
                        newTask.cancel()
                        result = currentTask.await()
                        break
                    }else{
                        yield()
                    }
                }else{
                    result = newTask.await()
                    break
                }
            }
            result
        }
    }

    fun cancel(){
        activeTask.get()?.cancel()
    }
}