package me.hong.kotlinspring.web.advice

data class CustomException(
    val customMessage: CustomMessage
) : RuntimeException()