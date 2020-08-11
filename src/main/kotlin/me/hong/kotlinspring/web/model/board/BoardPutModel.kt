package me.hong.kotlinspring.web.model.board

import me.hong.kotlinspring.data.entity.Board
import me.hong.kotlinspring.web.advice.UserSession
import java.time.LocalDateTime

class BoardPutModel {
  companion object {
    fun toBoard(req: BoardPutReq): Board {
      return Board(
          title = req.title,
          content = req.content,
          hitCount = 0L
      )
    }

    fun toRes(board: Board, userSession: UserSession): BoardPutRes {
      return BoardPutRes(
          id = board.id,
          title = board.title,
          content = board.content,
          hitCount = board.hitCount,
          userId = board.userId,
          userName = userSession.name,
          createdAt = board.createdAt,
          updatedAt = board.updatedAt
      )
    }
  }
}

data class BoardPutReq(
    val title: String,
    val content: String
)

data class BoardPutRes(
    val id: Long?,
    val title: String,
    val content: String,
    var hitCount: Long,
    val userId: Long?,
    val userName: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)