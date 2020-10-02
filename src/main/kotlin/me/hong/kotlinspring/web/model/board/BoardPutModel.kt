package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.web.advice.SigninUser
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

class BoardPutReq(
    title: String,
    content: String
) {
  @NotEmpty
  val title: String = title

  @NotEmpty
  val content: String = content

  fun toBoard(): Board {
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
    fun of(board: Board, signinUser: SigninUser): BoardPutRes {
      return BoardPutRes(
          id = board.id,
          title = board.title,
          content = board.content,
          createdBy = board.createdBy,
          createdName = signinUser.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}