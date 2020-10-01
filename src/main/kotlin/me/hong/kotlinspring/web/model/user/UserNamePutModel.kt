package me.hong.kotlinspring.web.model.user

import javax.validation.constraints.NotBlank

class UserNamePutReq(
    name: String
) {
  @NotBlank
  val name: String = name
}