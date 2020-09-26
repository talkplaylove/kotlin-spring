package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.domain.board.BoardDomain
import me.hong.kotlinspring.data.domain.board.BoardHitDomain
import me.hong.kotlinspring.data.domain.board.BoardReadDomain
import me.hong.kotlinspring.data.domain.board.BoardUserDomain
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardDomain: BoardDomain,
    private val boardHitDomain: BoardHitDomain,
    private val boardReadDomain: BoardReadDomain,
    private val boardUserDomain: BoardUserDomain
) {
  fun getBoards(page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.findBoards(page, size)

    val users = boardUserDomain.getBoardUsers(boards)
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardDomain.findBoards(word, page, size)

    val users = boardUserDomain.getBoardUsers(boards)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.findBoards(userId, page, size)

    val users = boardUserDomain.getBoardUsers(boards)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long): BoardDetailRes {
    val board = boardDomain.getActiveBoard(boardId)

    val user = boardUserDomain.getBoardUser(board.createdBy)
    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession): BoardDetailRes {
    val userId = userSession.id

    val board = boardDomain.getActiveBoard(boardId)
    val user = boardUserDomain.getBoardUser(board.createdBy)
    val read = boardReadDomain.optional(boardId, userId)

    return if (read.isPresent) {
      BoardDetailRes.of(board, user, read.get().likeOrHate)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  @Transactional
  fun createBoard(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.createBoard(req.toBoard())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.getActiveBoard(boardId)

    if (userSession.unmatches(board.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    boardDomain.updateBoard(board, req.toBoard())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun deleteBoard(boardId: Long, userSession: UserSession) {
    val board = boardDomain.getActiveBoard(boardId)

    if (userSession.unmatches(board.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    boardDomain.deactivateBoard(board)
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String) {
    if (boardHitDomain.optional(boardId, ip).isEmpty) {
      boardHitDomain.hit(boardId, ip)

      val board = boardDomain.getBoard(boardId)
      board.hit()
    }
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String, userSession: UserSession) {
    val userId = userSession.id
    this.hitBoard(boardId, ip)

    boardReadDomain.optional(boardId, userId).orElseGet {
      boardReadDomain.read(boardId, userId)
    }
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    val userId = userSession.id
    var read: BoardRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardReadDomain.optional(boardId, userId).ifPresentOrElse({
      currentLikeOrHate = it.likeOrHate
      if (it.likeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardReadDomain.read(boardId, userId, likeOrHate)
    })

    if (currentLikeOrHate == likeOrHate) {
      throw CustomException(CustomMessage.SAME_VALUES)
    }

    boardDomain.countLikeOrHateBoard(boardId, currentLikeOrHate, likeOrHate)

    return BoardLikeRes.of(read)
  }

  fun createBoardUser(userId: Long, userName: String) {
    boardUserDomain.createBoardUser(BoardUser(userId, userName))
  }
}