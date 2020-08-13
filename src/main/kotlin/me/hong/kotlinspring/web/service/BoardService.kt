package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.board.BoardLike
import me.hong.kotlinspring.data.entity.board.embedded.BoardLikeId
import me.hong.kotlinspring.data.repo.board.BoardLikeRepo
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.data.repo.user.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepo: BoardRepo,
    private val boardHitRepo: BoardLikeRepo,
    private val userRepo: UserRepo
) {
  fun get(boardId: Long): BoardDetailRes {
    val board = boardRepo.findById(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    val user = userRepo.findById(board.userId)

    return BoardDetailRes.of(board, user)
  }

  fun create(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.save(req.toEntity())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun update(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.findById(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.title = req.title
    board.content = req.content

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun delete(boardId: Long, userSession: UserSession) {
    val board = boardRepo.findById(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    return boardRepo.remove(board)
  }

  @Transactional
  fun likeOrHate(boardId: Long, req: BoardLIkeReq, userSession: UserSession): BoardLikeRes {
    val boardHitId = BoardLikeId(boardId, userSession.id)

    var boardLike: BoardLike? = null
    boardHitRepo.findById(boardHitId).ifPresentOrElse({
      if (it.likeOrHate == req.likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = req.likeOrHate
      boardLike = it
    }, {
      boardLike = boardHitRepo.insert(req.toEntity(boardHitId))
    })

    return BoardLikeRes.of(boardLike)
  }
}