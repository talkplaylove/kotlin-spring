package me.hong.kotlinspring.web.model.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class UserEmailPutReq(
  email: String
) {
  @NotBlank
  @Email
  val email: String = email
}