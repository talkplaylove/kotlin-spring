package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.enums.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardRead
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

class BoardLIkeReq(
    likeOrHate: LikeOrHate
) {
  @NotNull
  val likeOrHate: LikeOrHate = likeOrHate
}

data class BoardLikeRes(
    var likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(read: BoardRead?): BoardLikeRes {
      return BoardLikeRes(
          likeOrHate = read!!.likeOrHate,
          createdAt = read.createdAt,
          updatedAt = read.updatedAt
      )
    }
  }
}