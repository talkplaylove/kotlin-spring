package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.web.advice.UserSession
import java.time.LocalDateTime

data class BoardPutReq(
    val title: String,
    val content: String
) {
  fun toEntity(): Board {
    return Board(
        title = this.title,
        content = this.content
    )
  }
}

data class BoardPutRes(
    val id: Long?,
    val title: String,
    val content: String,
    val userId: Long?,
    val userName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, userSession: UserSession): BoardPutRes {
      return BoardPutRes(
          id = board.id,
          title = board.title,
          content = board.content,
          userId = board.userId,
          userName = userSession.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}