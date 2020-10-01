package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.data.constant.user.Gender
import me.hong.kotlinspring.data.entity.user.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class SignupReq(
    name: String,
    email: String,
    password: String,
    gender: Gender
) {
  @NotBlank
  val name: String = name

  @NotBlank
  @Email
  val email: String = email

  @NotBlank
  val password: String = password

  @NotNull
  val gender: Gender = gender

  fun toUser(encodedPassword: String): User {
    return User(
        email = this.email,
        name = this.name,
        password = encodedPassword,
        gender = this.gender
    )
  }
}

data class SignupRes(
    val id: Long?
) {
  companion object {
    fun of(user: User): SignupRes {
      return SignupRes(id = user.id)
    }
  }
}