package me.hong.kotlinspring.web.model.user

import me.hong.kotlinspring.constant.user.Gender

data class SigninReq(
    val email: String,
    val password: String
)

data class SigninRes(
    val id: Long?,
    val name: String,
    val gender: Gender
)