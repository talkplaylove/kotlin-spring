package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.Board
import me.hong.kotlinspring.data.entity.User
import java.time.LocalDateTime

class BoardDetailModel {
  companion object {
    fun toRes(board: Board, user: User?): BoardDetailRes {
      return BoardDetailRes(
          id = board.id,
          title = board.title,
          content = board.content,
          userId = board.userId,
          userName = user!!.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }

  }
}

data class BoardDetailRes(
    val id: Long?,
    val title: String,
    val content: String,
    val userId: Long?,
    val userName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)