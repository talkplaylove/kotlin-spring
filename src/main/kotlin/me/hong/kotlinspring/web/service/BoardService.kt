package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.Board
import me.hong.kotlinspring.data.repo.BoardRepo
import me.hong.kotlinspring.data.repo.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardReq
import me.hong.kotlinspring.web.model.board.BoardRes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepo: BoardRepo,
    private val userRepo: UserRepo
) {
  fun get(id: Long): BoardRes {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    val user = userRepo.findById(board.userId)

    return BoardRes(
        id = board.id,
        title = board.title,
        content = board.content,
        userId = board.userId,
        userName = user!!.name,
        createdAt = board.createdAt,
        updatedAt = board.updatedAt
    )
  }

  fun create(req: BoardReq, userSession: UserSession): BoardRes {
    val board = boardRepo.save(Board(
        req.title,
        req.content,
        false
    ))

    return BoardRes(
        id = board.id,
        title = board.title,
        content = board.content,
        userId = board.userId,
        userName = userSession.name,
        createdAt = board.createdAt,
        updatedAt = board.updatedAt
    )
  }

  @Transactional
  fun update(id: Long, req: BoardReq, userSession: UserSession): BoardRes {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.title = req.title
    board.content = req.content

    return BoardRes(
        id = board.id,
        title = board.title,
        content = board.content,
        userId = board.userId,
        userName = userSession.name,
        createdAt = board.createdAt,
        updatedAt = board.updatedAt
    )
  }

  @Transactional
  fun delete(id: Long, userSession: UserSession) {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.deleted = true
  }
}