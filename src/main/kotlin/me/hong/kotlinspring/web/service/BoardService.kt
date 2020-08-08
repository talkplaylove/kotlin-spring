package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.Board
import me.hong.kotlinspring.data.repo.BoardRepo
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
    private val userSession: UserSession
) {
  fun get(id: Long): BoardRes {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    return BoardRes(
        board.id,
        board.title,
        board.content,
        board.userId,
        board.createdAt,
        board.updatedAt
    )
  }

  fun create(req: BoardReq): BoardRes {
    val board = boardRepo.save(Board(
        req.title,
        req.content,
        false
    ))

    return BoardRes(
        board.id,
        board.title,
        board.content,
        board.userId,
        board.createdAt,
        board.updatedAt
    )
  }

  @Transactional
  fun update(id: Long, req: BoardReq): BoardRes {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    if (userSession.matches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.title = req.title
    board.content = req.content

    return BoardRes(
        board.id,
        board.title,
        board.content,
        board.userId,
        board.createdAt,
        board.updatedAt
    )
  }

  @Transactional
  fun delete(id: Long) {
    val board = boardRepo.findByIdAndDeleted(id, false)
        ?: throw CustomException(CustomMessage.BOARD_NOT_FOUND)

    if (userSession.matches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.deleted = true
  }
}