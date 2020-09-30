package me.hong.kotlinspring.web.model.user

import javax.validation.constraints.NotBlank

data class UserNamePutReq(
    @NotBlank
    val name: String
)