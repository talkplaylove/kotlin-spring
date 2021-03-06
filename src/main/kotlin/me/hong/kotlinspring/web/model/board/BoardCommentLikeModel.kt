package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.enums.board.LikeOrHate
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

class BoardCommentLIkeReq(
  likeOrHate: LikeOrHate
) {
  @NotNull
  val likeOrHate: LikeOrHate = likeOrHate
}

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