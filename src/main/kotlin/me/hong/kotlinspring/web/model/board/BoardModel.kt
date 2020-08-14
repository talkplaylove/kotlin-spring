package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.user.User
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class BoardRes(
    val id: Long?,
    val title: String,
    val userId: Long?,
    val userName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
  companion object {
    fun of(board: Board, user: User?): BoardRes {
      return BoardRes(
          id = board.id,
          title = board.title,
          userId = board.userId,
          userName = user!!.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }

    fun listOf(boards: List<Board>, users: Map<Long?, User>): List<BoardRes> {
      val res = mutableListOf<BoardRes>()
      boards.forEach {
        res.add(of(it, users[it.userId]))
      }
      return res
    }
  }
}