package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.data.constant.user.Gender
import me.hong.kotlinspring.data.entity.user.User
import javax.validation.constraints.NotBlank

data class SigninReq(
    @NotBlank
    val email: String,
    @NotBlank
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