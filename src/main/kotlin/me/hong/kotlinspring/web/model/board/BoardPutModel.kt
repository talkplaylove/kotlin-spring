package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.web.advice.UserSession
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

data class BoardPutReq(
    @NotEmpty
    val title: String,

    @NotEmpty
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
    val createdBy: Long?,
    val createdName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, userSession: UserSession): BoardPutRes {
      return BoardPutRes(
          id = board.id,
          title = board.title,
          content = board.content,
          createdBy = board.createdBy,
          createdName = userSession.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}