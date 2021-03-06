package me.hong.kotlinspring.web.advice

import org.springframework.http.HttpStatus

enum class CustomMessage(val status: HttpStatus, val message: String) {
  ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류입니다."),

  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자 인증이 필요합니다."),

  FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

  AT_LEAST_TWO_LETTERS(HttpStatus.BAD_REQUEST, "검색어는 두 글자 이상이어야 합니다."),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INCORRECT_PASSWORD(HttpStatus.NOT_FOUND, "패스워드가 틀립니다."),
  BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

  SAME_VALUES(HttpStatus.CONFLICT, "수정 값이 동일합니다."),
  EXISTS_EMAIL(HttpStatus.CONFLICT, "중복되는 이메일입니다."),
  EXISTS_NAME(HttpStatus.CONFLICT, "중복되는 이름입니다."),
}