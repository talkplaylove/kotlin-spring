package me.hong.kotlinspring.web.model.user

import javax.validation.constraints.NotBlank

data class UserPasswordPutReq(
    @NotBlank
    val password: String
)