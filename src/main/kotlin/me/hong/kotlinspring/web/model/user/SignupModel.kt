package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.data.constant.user.Gender
import me.hong.kotlinspring.data.entity.user.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SignupReq(
    @NotBlank
    val name: String,
    @NotBlank
    val email: String,
    @NotBlank
    val password: String,
    @NotNull
    val gender: Gender
) {
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