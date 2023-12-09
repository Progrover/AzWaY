@file:JvmName("Observer")
package com.msaharov.azway

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ObserveBuilder<T>(
    private var _stateFlow: StateFlow<T>,
    private var _lifecycleOwner: LifecycleOwner? = null,
    private var _state: Lifecycle.State = Lifecycle.State.STARTED
) {

    fun repeatOn(state: Lifecycle.State): ObserveBuilder<T> {
        _state = state
        return this
    }

    fun onReceiveWithScope(block: suspend (T) -> Unit) {
        _lifecycleOwner?.lifecycleScope?.launch {
            _lifecycleOwner?.lifecycle?.repeatOnLifecycle(_state) {
                _stateFlow.collect { data -> block(data) }
            }
        }
    }

    fun onReceive(block: (T) -> Unit) {
        _lifecycleOwner?.lifecycleScope?.launch {
            _lifecycleOwner?.lifecycle?.repeatOnLifecycle(_state) {
                _stateFlow.collect { data -> block(data) }
            }
        }
    }
}

fun <T> Activity.installObserver(flow: StateFlow<T>): ObserveBuilder<T> {
    return ObserveBuilder<T>(_stateFlow = flow, _lifecycleOwner = this as LifecycleOwner)
}

fun <T> StateFlow<T>.installObserver(activity: Activity): ObserveBuilder<T> {
    return ObserveBuilder<T>(_stateFlow = this, _lifecycleOwner = activity as LifecycleOwner)
}

fun <T> Fragment.installObserver(flow: StateFlow<T>): ObserveBuilder<T> {
    return ObserveBuilder<T>(_stateFlow = flow, _lifecycleOwner = this.viewLifecycleOwner)
}

fun <T> StateFlow<T>.installObserver(fragment: Fragment): ObserveBuilder<T> {
    return ObserveBuilder<T>(_stateFlow = this, _lifecycleOwner = fragment.viewLifecycleOwner)
}