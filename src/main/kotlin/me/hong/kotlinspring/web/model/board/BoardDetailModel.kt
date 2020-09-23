package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.entity.user.User
import java.time.LocalDateTime

data class BoardDetailRes(
    val id: Long?,
    val title: String,
    val content: String,
    val createdBy: Long?,
    val createdName: String,
    val likeOrHate: LikeOrHate,
    val likeCount: Long,
    val hateCount: Long,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, user: BoardUser?): BoardDetailRes {
      return this.of(board, user, LikeOrHate.NONE)
    }

    fun of(board: Board, user: BoardUser?, likeOrHate: LikeOrHate): BoardDetailRes {
      return BoardDetailRes(
          id = board.id,
          title = board.title,
          content = board.content,
          createdBy = board.createdBy,
          createdName = user!!.userName,
          likeOrHate = likeOrHate,
          likeCount = board.likeCount,
          hateCount = board.hateCount,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}