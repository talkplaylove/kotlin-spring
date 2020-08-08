package me.hong.kotlinspring.web.model.board

import java.time.LocalDateTime

data class BoardReq(
    val title: String,
    val content: String
)

data class BoardRes(
    val id: Long?,
    val title: String,
    val content: String,
    val userId: Long?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)