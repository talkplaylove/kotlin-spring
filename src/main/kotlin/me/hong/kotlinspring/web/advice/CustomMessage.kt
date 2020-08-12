package me.hong.kotlinspring.web.advice

import org.springframework.http.HttpStatus

enum class CustomMessage(val status: HttpStatus, val message: String) {
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INCORRECT_PASSWORD(HttpStatus.NOT_FOUND, "패스워드가 틀립니다."),

  SAME_VALUES(HttpStatus.CONFLICT, "수정 값이 동일합니다."),
  BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
}