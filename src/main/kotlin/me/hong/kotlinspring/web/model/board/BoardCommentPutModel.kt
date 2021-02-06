package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.web.advice.SigninUser
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

class BoardCommentPutReq(
  content: String
) {
  @NotEmpty
  val content: String = content

  fun toBoardComment(boardId: Long): BoardComment {
    return BoardComment(
      boardId = boardId,
      content = this.content
    )
  }
}

data class BoardCommentPutRes(
  val id: Long?,
  val boardId: Long,
  val content: String,
  val createdBy: Long?,
  val createdName: String,
  val createdAt: LocalDateTime?,
  val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(comment: BoardComment, signinUser: SigninUser): BoardCommentPutRes {
      return BoardCommentPutRes(
        id = comment.id,
        boardId = comment.boardId,
        content = comment.content,
        createdBy = comment.createdBy,
        createdName = signinUser.name,
        createdAt = comment.createdAt,
        updatedAt = comment.updatedAt
      )
    }
  }
}