package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.repo.BoardRepo
import me.hong.kotlinspring.data.repo.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepo: BoardRepo,
    private val userRepo: UserRepo
) {
  fun get(id: Long): BoardDetailRes {
    val board = boardRepo.findById(id).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    val user = userRepo.findById(board.userId)

    return BoardDetailModel.toRes(board, user)
  }

  fun create(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.save(BoardPutModel.toBoard(req))

    return BoardPutModel.toRes(board, userSession)
  }

  @Transactional
  fun update(id: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.findById(id).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.title = req.title
    board.content = req.content

    return BoardPutModel.toRes(board, userSession)
  }

  @Transactional
  fun delete(id: Long, userSession: UserSession) {
    val board = boardRepo.findById(id).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    boardRepo.remove(board)
  }
}