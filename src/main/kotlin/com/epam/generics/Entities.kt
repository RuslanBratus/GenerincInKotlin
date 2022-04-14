package com.epam.generics

/**
 * Some data classes for running examples of Transformer Task
 */

data class State(
        val data: Data? = null,
        val error: Error? = null
)

data class Error(
        val code: Int = 0,
        val message: String? = null
)

data class Data(val value: Number)