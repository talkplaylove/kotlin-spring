package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardRead
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class BoardLIkeReq(
    @NotNull
    var likeOrHate: LikeOrHate
)

data class BoardLikeRes(
    var likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(boardRead: BoardRead?): BoardLikeRes {
      return BoardLikeRes(
          likeOrHate = boardRead!!.likeOrHate,
          createdAt = boardRead.createdAt,
          updatedAt = boardRead.updatedAt
      )
    }
  }
}