package com.epam.generics

/**
 * TODO Write generic class Optional which allows
 * - to check whether [value] which it was created with [exists] or null
 * - retrieve value if it is not null [get]
 * - in case value is null get should cause NullPointerException
 * - retrieve nullable version of value [getOrNull]
 *
 * There should be a companion object extension method [toOptional] which allows
 * to create Optional object from `Any?` object
 */
class Optional<T : Any?> private constructor(
    private val value : T?
) {
    val exists: Boolean = value != null

    fun get(): T = value?: throw NullPointerException()

    fun getOrNull(): T? = value

    companion object {
        fun <R> R?.toOptional(): Optional<R> = Optional(this)
    }
}





