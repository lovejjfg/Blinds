package com.lovejjfg.demo

/**
 * Created by joe on 2019/2/18.
 * Email: lovejjfg@gmail.com
 */
class Test {
    fun addition_isCorrect() {
        val test = "2"
        var test2 = "xxx"
        test2 = "hahahaha"
        test2.capitalize()
        A.TestInternal()
    }

    interface B {
        fun f() {
            print("B")
        } // 接口成员默认就是“open”的

        fun b() {
            print("b")
        }
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
    open fun f() {
        print("A")
    }

    fun a() {
        print("a")
        TestInternal()
    }

    internal class TestInternal {
        fun test() {
        }
    }
}
