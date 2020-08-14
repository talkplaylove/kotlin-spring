package me.hong.kotlinspring.web.advice

data class CustomException(
    var customMessage: CustomMessage
) : RuntimeException()