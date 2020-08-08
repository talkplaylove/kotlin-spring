package me.hong.kotlinspring.web.advice

import me.hong.kotlinspring.web.model.ErrorModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExeptionHandler {

  @ExceptionHandler(Exception::class)
  fun handle(exception: Exception): ResponseEntity<ErrorModel> {
    return ResponseEntity(exception.message?.let { ErrorModel(it) }, HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(CustomException::class)
  fun handle(exception: CustomException): ResponseEntity<ErrorModel> {
    val errorMessage = exception.customMessage
    return ResponseEntity(ErrorModel(errorMessage.message), errorMessage.status)
  }
}