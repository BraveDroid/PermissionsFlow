package com.aallam.permissionsflow.sample.extension

import android.view.View
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
internal fun View.clicks(): Flow<Unit> {
    return callbackFlow {
        val listener = View.OnClickListener {
            safeOffer(Unit)
        }
        setOnClickListener(listener)
        awaitClose { setOnClickListener(null) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    return !isClosedForSend && try {
        offer(value)
    } catch (e: CancellationException) {
        false
    }
}
