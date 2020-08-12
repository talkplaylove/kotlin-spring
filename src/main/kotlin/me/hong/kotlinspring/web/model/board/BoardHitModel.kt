package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import java.time.LocalDateTime

data class BoardHitReq(
    var likeOrHate: LikeOrHate
) {
  fun toEntity(boardHitId: BoardHitId): BoardHit {
    return BoardHit(
        id = boardHitId,
        likeOrHate = this.likeOrHate
    )
  }
}

data class BoardHitRes(
    var likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(boardHit: BoardHit?): BoardHitRes {
      return BoardHitRes(
          likeOrHate = boardHit!!.likeOrHate,
          createdAt = boardHit.createdAt,
          updatedAt = boardHit.updatedAt
      )
    }
  }
}