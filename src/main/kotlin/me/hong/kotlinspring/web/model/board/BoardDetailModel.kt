package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.user.User
import java.time.LocalDateTime

data class BoardDetailRes(
    val id: Long?,
    val title: String,
    val content: String,
    val createdBy: Long?,
    val createdName: String,
    val likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, user: User?): BoardDetailRes {
      return of(board, user, LikeOrHate.NONE)
    }

    fun of(board: Board, user: User?, likeOrHate: LikeOrHate): BoardDetailRes {
      return BoardDetailRes(
          id = board.id,
          title = board.title,
          content = board.content,
          createdBy = board.createdBy,
          createdName = user!!.name,
          likeOrHate = likeOrHate,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}