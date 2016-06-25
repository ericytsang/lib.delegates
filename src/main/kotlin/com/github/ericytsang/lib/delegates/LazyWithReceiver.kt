package com.github.ericytsang.lib.delegates

import java.util.WeakHashMap
import kotlin.reflect.KProperty

/**
 * Created by surpl on 5/5/2016.
 */
class LazyWithReceiver<This,Return>(val initializer:(thisRef:This)->Return)
{
    private val rToState = WeakHashMap<This,State<This,Return>>()

    operator fun getValue(thisRef:Any?,property:KProperty<*>):Return
    {
        @Suppress("UNCHECKED_CAST","NAME_SHADOWING")
        val thisRef = thisRef as This
        val state = rToState.getOrPut(thisRef,{UninitializedState()})
        return state.getValue(thisRef,property)
    }

    interface State<This,Return>
    {
        fun getValue(thisRef:This, property:KProperty<*>):Return
    }

    private inner class UninitializedState:State<This,Return>
    {
        override fun getValue(thisRef:This,property:KProperty<*>):Return
        {
            synchronized(this@LazyWithReceiver)
            {
                if (rToState[thisRef] === this)
                {
                    rToState[thisRef] = InitializedState(initializer(thisRef))
                }
            }
            return this@LazyWithReceiver.getValue(thisRef,property)
        }
    }

    private inner class InitializedState(val value:Return):State<This,Return>
    {
        override fun getValue(thisRef:This,property:KProperty<*>):Return = value
    }
}
