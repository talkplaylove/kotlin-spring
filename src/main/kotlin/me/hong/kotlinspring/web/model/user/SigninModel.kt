package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.constant.user.Gender
import me.hong.kotlinspring.data.entity.user.User

data class SigninReq(
    val email: String,
    val password: String
)

data class SigninRes(
    val id: Long?,
    val name: String,
    val gender: Gender
) {
  companion object {
    fun of(user: User): SigninRes {
      return SigninRes(
          id = user.id,
          name = user.name,
          gender = user.gender
      )
    }
  }
}