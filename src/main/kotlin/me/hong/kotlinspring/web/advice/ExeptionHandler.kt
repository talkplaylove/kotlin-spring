package me.hong.kotlinspring.web.advice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExeptionHandler {

  @ExceptionHandler(LogicException::class)
  fun handle(exception: LogicException): ResponseEntity<ErrorRes> {
    val errorMessage = exception.errorMessage
    return ResponseEntity(ErrorRes(errorMessage.message), errorMessage.status)
  }
}