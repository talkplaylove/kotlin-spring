package me.hong.kotlinspring.web.advice

import me.hong.kotlinspring.web.model.ErrorRes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.ServletException
import javax.validation.ValidationException

@RestControllerAdvice
class ExeptionHandler {

  val logger: Logger = LoggerFactory.getLogger(this.javaClass)

  @ExceptionHandler(Exception::class)
  fun handle(exception: Exception): ResponseEntity<ErrorRes> {
    val customMessage = CustomMessage.ERROR
    logger.error(customMessage.message, exception)
    return ResponseEntity(ErrorRes.of(customMessage), customMessage.status)
  }

  @ExceptionHandler(ValidationException::class)
  fun handle(exception: ValidationException): ResponseEntity<ErrorRes> {
    val status = HttpStatus.BAD_REQUEST
    return ResponseEntity(ErrorRes.of(status.name, exception.message), status)
  }

  @ExceptionHandler(ServletException::class)
  fun handle(exception: ServletException): ResponseEntity<ErrorRes> {
    val status = HttpStatus.BAD_REQUEST
    return ResponseEntity(ErrorRes.of(status.name, exception.message), status)
  }

  @ExceptionHandler(CustomException::class)
  fun handle(exception: CustomException): ResponseEntity<ErrorRes> {
    val customMessage = exception.customMessage
    return ResponseEntity(ErrorRes.of(customMessage), customMessage.status)
  }
}