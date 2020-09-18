package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.user.User
import java.time.LocalDateTime

data class BoardRes(
    val id: Long?,
    val title: String,
    val createdBy: Long?,
    val createdName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, user: User?): BoardRes {
      return BoardRes(
          id = board.id,
          title = board.title,
          createdBy = board.createdBy,
          createdName = user!!.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }

    fun listOf(boards: List<Board>, users: Map<Long?, User>): Collection<BoardRes> {
      val res = mutableListOf<BoardRes>()
      boards.forEach {
        res.add(this.of(it, users[it.createdBy]))
      }
      return res
    }
  }
}