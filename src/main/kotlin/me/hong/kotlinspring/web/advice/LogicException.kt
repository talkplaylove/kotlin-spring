package me.hong.kotlinspring.web.advice

data class LogicException(
    val errorMessage: ErrorMessage
) : RuntimeException()