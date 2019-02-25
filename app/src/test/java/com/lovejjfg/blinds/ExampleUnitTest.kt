package com.lovejjfg.blinds

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val test = "2"
        var test2 = "xxx"
        test2 = "hahahaha"
        test2.capitalize()
        A.TestInternal()
    }


    interface B {
        fun f() { print("B") } // 接口成员默认就是“open”的
        fun b() { print("b") }
    }

    class C() : A(), B {
        // 编译器要求覆盖 f()：
        override fun f() {
            super<A>.f() // 调用 A.f()
            super<B>.f() // 调用 B.f()
        }
    }
}

open class A {
    open fun f() { print("A") }
    fun a() { print("a")
        TestInternal()
    }
    internal class TestInternal {
        fun test() {

        }
    }
}
