package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.web.validation.Password

class UserPasswordPutReq(
    password: String
) {
  @Password
  val password: String = password
}