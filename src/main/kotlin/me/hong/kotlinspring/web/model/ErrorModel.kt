package me.hong.kotlinspring.web.model

import me.hong.kotlinspring.web.advice.CustomMessage

data class ErrorRes(
    val name: String,
    val message: String
) {
  companion object {
    fun of(customMessage: CustomMessage): ErrorRes {
      return ErrorRes(
          name = customMessage.name,
          message = customMessage.message
      )
    }

    fun of(name: String, message: String?): ErrorRes {
      return ErrorRes(
          name = name,
          message = message!!
      )
    }
  }
}