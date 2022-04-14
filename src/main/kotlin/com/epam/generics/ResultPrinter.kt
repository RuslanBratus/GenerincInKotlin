package com.epam.generics

/**
 * Class which prints results of optional values
 * If value exists (non-null) it prints: "No value"
 * Otherwise calls standard toString method
 */
open class ResultPrinter<T : Any?>(private val value: Optional<T>) {
    open fun print() {
        if (value.exists) {
            println(value.get())
        } else {
            println("No value")
        }
    }
}