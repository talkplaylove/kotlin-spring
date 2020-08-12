package me.hong.kotlinspring.web.model.user

data class UserDuplicateRes(
    val duplicated: Boolean
) {
  companion object {
    fun of(duplicated: Boolean): UserDuplicateRes {
      return UserDuplicateRes(duplicated)
    }
  }
}