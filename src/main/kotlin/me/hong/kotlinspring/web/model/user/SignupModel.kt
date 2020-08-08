package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.constant.Gender

data class SignupReq(
    val name: String,
    val email: String,
    val password: String,
    val gender: Gender
)

data class SignupRes(
    val id: Long?
)