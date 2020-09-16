package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.data.entity.user.User
import java.time.LocalDateTime

data class BoardCommentRes(
    val id: Long?,
    val boardId: Long,
    var content: String,
    val createdBy: Long?,
    val createdName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(comment: BoardComment, user: User?): BoardCommentRes {
      return BoardCommentRes(
          id = comment.id,
          boardId = comment.boardId,
          content = comment.content,
          createdBy = comment.createdBy,
          createdName = user!!.name,
          createdAt = comment.createdAt,
          updatedAt = comment.updatedAt
      )
    }

    fun listOf(comments: List<BoardComment>, users: Map<Long?, User>): Collection<BoardCommentRes> {
      var res = mutableListOf<BoardCommentRes>()
      comments.forEach {
        res.add(BoardCommentRes.of(it, users[it.createdBy]))
      }
      return res
    }
  }
}