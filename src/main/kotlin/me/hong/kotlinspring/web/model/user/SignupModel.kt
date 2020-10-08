package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.data.entity.user.User
import me.hong.kotlinspring.data.enums.user.Gender
import me.hong.kotlinspring.web.validation.Password
import me.hong.kotlinspring.web.validation.UserName
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class SignupReq(
    name: String,
    email: String,
    password: String,
    gender: Gender
) {
  @UserName
  val name: String = name

  @NotBlank
  @Email
  val email: String = email

  @Password
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