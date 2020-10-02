package me.hong.kotlinspring.web.model

import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.http.HttpStatus

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

    fun of(status: HttpStatus, message: String): ErrorRes {
      return ErrorRes(
          name = status.name,
          message = message
      )
    }
  }
}