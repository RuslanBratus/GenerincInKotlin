package com.epam.generics

import com.epam.generics.Optional.Companion.toOptional

/**
 * This task is optional.
 * If you don't want to do it just change the flag below to false
 */
const val CHECK_UTIL_TASK = true

/**
 * TODO write an utility function [printTo] which simplifies work with Transformer class.
 *
 * If you check [main] function in Task2 you may notice that some of things
 * are repeated or inconvenient.
 *
 * In case we have a large [State] with many nested nullable substates
 * we can prefer to avoid accessing it directly and converting each of them
 * to Optional. Moreover we would like to provide ResultPrinter into this
 * function and print results on it.
 *
 * Please check [main] function below to check the expected result
 */

fun <T : Any, R : Any> Transformer<T>.printTo(

    printerFactory: (Optional<R>) -> ResultPrinter<R>,
    selector: T.() -> R?

) = map {selector(it).toOptional()}.transform {printerFactory(it).print()}


/**
 * Code below is a specification and should compile and work as described
 */
fun main() {
    val source = Transformer.Source<State>()
    Transformer(source).printTo({ ResultPrinter(it) }) { data }


    source.send(State()) // -> "No value"
    source.send(State(data = Data(1))) // -> "Data(value=1)"

    val errorSource = Transformer.Source<State>()
    Transformer(errorSource).printTo({ ResultPrinter(it) }) { error?.message }
    errorSource.send(State()) // -> "No value"
    errorSource.send(State(error = Error(0, null))) // -> "No value"
    errorSource.send(State(
            error = Error(0, "Generics are awesome")
    )) // -> "Generics are awesome"
}