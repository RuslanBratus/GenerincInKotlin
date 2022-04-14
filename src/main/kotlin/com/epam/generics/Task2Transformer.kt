package com.epam.generics

import com.epam.generics.Optional.Companion.toOptional

/**
 * TODO Finish Transformer class
 * This class allows to chain several conversion operations.
 * Whenever new value is sent to provided [source] the whole chain should made appropriate conversions
 * and return expected result in lambda provided to [transform] method.
 *
 * Transformer class does not support nullable values (same as Observable in RxJava).
 * Otherwise NullPointerException will be thrown during conversions.
 *
 * For convenience [Optional] type is used and results are printed using [ResultPrinter]
 *
 * Please check [main] method for the reference how it should work
     */
class Transformer<T>(private val source: Source<T>) {

    init {
        source.subscribe(this)
    }

    private var onNext: ((T) -> Any?)? /*TODO Add type*/ = null
    private var next: Transformer<Any>? = null
    private var action: (Any) -> T = { it as T }

    fun <R> transform(onNext: (T) -> R) /*TODO Add type*/ {
        this.onNext = onNext
    }

    fun <R : Any> map(arg: (T) -> R?): Transformer<R> =
        Transformer(Source<R>()).also {
            this.action = arg as (Any) -> T
            next = it as Transformer<Any>
        }


    private fun next(value: T) {
        val next = next
        if (next == null) {
            onNext?.invoke(action(value!!))
        } else {
            next.source.send(action(value!!)!!)
        }
    }

    class Source<S> {
        private var listener: Transformer<S>? = null

        fun subscribe(transformer: Transformer<S>) {
            this.listener = transformer
        }

        fun send(value: S) {
            listener?.next(value)
        }
    }
}

/**
 * Code below is a specification and should compile and work as described
 */
fun main() {
    val source = Transformer.Source<State>()
    Transformer(source)
            .map { it.data.toOptional() }
            .transform {
                ResultPrinter(it).print()
            }
    source.send(State()) // -> "No value"
    source.send(State(data = Data(1))) // -> "Data(value=1)"

    val errorSource = Transformer.Source<Error>()
    Transformer(errorSource)
            .map { it.message.toOptional() }
            .transform {
                ResultPrinter(it).print()
            }
    errorSource.send(Error()) // -> "No value"
    errorSource.send(Error(message = "Generics are awesome")) // -> "Generics are awesome"

    Transformer(source)
            .map { (it.error?.message).toOptional() }
            .transform {
                ResultPrinter(it).print()
            }
    source.send(State()) // -> "No value"
    source.send(State(error = Error(0, null))) // -> "No value"
    source.send(State(
            error = Error(0, "Generics are awesome")
    )) // -> "Generics are awesome"
}
