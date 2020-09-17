package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class BoardCommentLIkeReq(
    @NotNull
    var likeOrHate: LikeOrHate
)

data class BoardCommentLikeRes(
    var likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(read: BoardCommentRead?): BoardCommentLikeRes {
      return BoardCommentLikeRes(
          likeOrHate = read!!.likeOrHate,
          createdAt = read.createdAt,
          updatedAt = read.updatedAt
      )
    }
  }
}