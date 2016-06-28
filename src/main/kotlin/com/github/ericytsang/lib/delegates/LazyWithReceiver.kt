package com.github.ericytsang.lib.delegates

import java.util.WeakHashMap
import kotlin.reflect.KProperty

    class LazyWithReceiver<This,Return>(val initializer:This.()->Return)
    {
        private val values = WeakHashMap<This,Return>()

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(thisRef:Any,property:KProperty<*>):Return = synchronized(values)
        {
            thisRef as This
            return values.getOrPut(thisRef) {thisRef.initializer()}
        }
    }
