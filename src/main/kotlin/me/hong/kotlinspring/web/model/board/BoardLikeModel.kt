package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardLike
import me.hong.kotlinspring.data.entity.board.embedded.BoardLikeId
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class BoardLIkeReq(
    @NotNull
    var likeOrHate: LikeOrHate
) {
  fun toEntity(boardLikeId: BoardLikeId): BoardLike {
    return BoardLike(
        id = boardLikeId,
        likeOrHate = this.likeOrHate
    )
  }
}

data class BoardLikeRes(
    var likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(boardLike: BoardLike?): BoardLikeRes {
      return BoardLikeRes(
          likeOrHate = boardLike!!.likeOrHate,
          createdAt = boardLike.createdAt,
          updatedAt = boardLike.updatedAt
      )
    }
  }
}