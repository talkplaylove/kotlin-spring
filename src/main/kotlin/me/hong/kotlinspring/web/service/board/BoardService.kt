package me.hong.kotlinspring.web.service.board

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
import java.util.stream.Collectors

@Service
class BoardService(
    private val boardDomain: BoardDomain,
    private val boardHitDomain: BoardHitDomain,
    private val boardReadDomain: BoardReadDomain,
    private val boardUserDomain: BoardUserDomain
) {
  fun getBoards(page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.getActivePage(page, size)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardDomain.getActivePage(word, page, size)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.getActivePage(userId, page, size)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long): BoardDetailRes {
    val board = boardDomain.getActiveOne(boardId)

    val user = boardUserDomain.getOne(board.createdBy)
    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession): BoardDetailRes {
    val userId = userSession.id

    val board = boardDomain.getActiveOne(boardId)
    val user = boardUserDomain.getOne(board.createdBy)
    val read = boardReadDomain.getOptional(boardId, userId)

    return if (read.isPresent) {
      BoardDetailRes.of(board, user, read.get().likeOrHate)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  @Transactional
  fun createBoard(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.create(req.toBoard())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.getActiveOne(boardId)

    userSession.unmatchesThrow(board.createdBy)

    boardDomain.update(board, req.toBoard())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun deleteBoard(boardId: Long, userSession: UserSession) {
    val board = boardDomain.getActiveOne(boardId)

    userSession.unmatchesThrow(board.createdBy)

    boardDomain.deactivate(board)
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String) {
    if (boardHitDomain.getOptional(boardId, ip).isEmpty) {
      boardHitDomain.hit(boardId, ip)

      val board = boardDomain.getOne(boardId)
      board.hit()
    }
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String, userSession: UserSession) {
    val userId = userSession.id
    this.hitBoard(boardId, ip)

    boardReadDomain.getOptional(boardId, userId).orElseGet {
      boardReadDomain.read(boardId, userId)
    }
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    val userId = userSession.id
    var read: BoardRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardReadDomain.getOptional(boardId, userId).ifPresentOrElse({
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

    boardDomain.countLikeOrHate(boardId, currentLikeOrHate, likeOrHate)

    return BoardLikeRes.of(read)
  }

  fun createBoardUser(userId: Long, userName: String) {
    boardUserDomain.create(BoardUser(userId, userName))
  }
}