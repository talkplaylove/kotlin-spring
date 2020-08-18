package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.user.User
import java.time.LocalDateTime

data class BoardDetailRes(
    val id: Long?,
    val title: String,
    val content: String,
    val userId: Long?,
    val userName: String,
    val likeOrHate: LikeOrHate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, user: User?): BoardDetailRes {
      return of(board, LikeOrHate.NONE, user)
    }

    fun of(board: Board, likeOrHate: LikeOrHate, user: User?): BoardDetailRes {
      return BoardDetailRes(
          id = board.id,
          title = board.title,
          content = board.content,
          userId = board.userId,
          userName = user!!.name,
          likeOrHate = likeOrHate,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}