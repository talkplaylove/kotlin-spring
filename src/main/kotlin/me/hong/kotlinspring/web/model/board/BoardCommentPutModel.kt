package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.web.advice.UserSession
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

data class BoardCommentPutReq(
    @NotEmpty
    val content: String
) {
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
    fun of(comment: BoardComment, userSession: UserSession): BoardCommentPutRes {
      return BoardCommentPutRes(
          id = comment.id,
          boardId = comment.boardId,
          content = comment.content,
          createdBy = comment.createdBy,
          createdName = userSession.name,
          createdAt = comment.createdAt,
          updatedAt = comment.updatedAt
      )
    }
  }
}