package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.data.entity.board.BoardUser
import java.time.LocalDateTime

data class BoardCommentRes(
    val id: Long?,
    val boardId: Long,
    var content: String,
    var likeCount: Long,
    var hateCount: Long,
    val createdBy: Long?,
    val createdName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(comment: BoardComment, user: BoardUser?): BoardCommentRes {
      return BoardCommentRes(
          id = comment.id,
          boardId = comment.boardId,
          content = comment.content,
          likeCount = comment.likeCount,
          hateCount = comment.hateCount,
          createdBy = comment.createdBy,
          createdName = user?.userName ?: "",
          createdAt = comment.createdAt,
          updatedAt = comment.updatedAt
      )
    }

    fun listOf(comments: List<BoardComment>, users: Map<Long?, BoardUser>): Collection<BoardCommentRes> {
      val res = mutableListOf<BoardCommentRes>()
      comments.forEach {
        res.add(this.of(it, users[it.createdBy]))
      }
      return res
    }
  }
}