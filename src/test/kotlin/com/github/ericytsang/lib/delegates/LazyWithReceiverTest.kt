package com.github.ericytsang.lib.delegates

import org.junit.Test

/**
 * Created by surpl on 6/28/2016.
 */
class LazyWithReceiverTest
{
    private var initializationCount = 0

    private val Int.timesTwo:Int by LazyWithReceiver<Int,Int>()
    {
        initializationCount += 1
        println("initializing!")
        this * 2
    }

    /**
     * this test case should print the following:
     * initializing!
     * 8
     * 8
     * initializing!
     * 10
     * initializing!
     * 12
     * 8
     * 10
     * 12
     */
    @Test
    fun test1()
    {
        println(4.timesTwo)
        assert(initializationCount == 1)
        println(4.timesTwo)
        assert(initializationCount == 1)
        println(5.timesTwo)
        assert(initializationCount == 2)
        println(6.timesTwo)
        assert(initializationCount == 3)
        println(4.timesTwo)
        println(5.timesTwo)
        println(6.timesTwo)
        assert(initializationCount == 3)
    }
}
