package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.web.validation.UserName

class UserNamePutReq(
    name: String
) {
  @UserName
  val name: String = name
}