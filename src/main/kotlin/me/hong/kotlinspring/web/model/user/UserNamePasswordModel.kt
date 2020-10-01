package me.hong.kotlinspring.web.model.user

import javax.validation.constraints.NotBlank

class UserPasswordPutReq(
    password: String
) {
  @NotBlank
  val password: String = password
}