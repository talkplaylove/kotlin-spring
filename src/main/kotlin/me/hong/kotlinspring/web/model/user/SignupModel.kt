package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.constant.user.Gender
import me.hong.kotlinspring.data.entity.user.User

data class SignupReq(
    val name: String,
    val email: String,
    val password: String,
    val gender: Gender
) {
  fun toEntity(encodedPassword: String): User {
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