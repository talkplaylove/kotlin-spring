package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.domain.BoardDomain
import me.hong.kotlinspring.web.model.board.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class BoardService(
    private val boardDomain: BoardDomain,
    private val userService: UserService
) {
  fun getBoards(page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.findBoards(page, size)

    val users = userService.getUsers(
        ids = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardDomain.findBoards(word, page, size)

    val users = userService.getUsers(
        ids = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.findBoards(userId, page, size)

    val users = userService.getUsers(
        boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    )

    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long): BoardDetailRes {
    val board = boardDomain.getActiveBoard(boardId)

    val user = userService.getUser(board.createdBy)

    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession): BoardDetailRes {
    val board = boardDomain.getActiveBoard(boardId)

    val user = userService.getUser(board.createdBy)

    val read = boardDomain.optionalBoardRead(boardId, userSession.id)

    return if (read.isPresent) {
      BoardDetailRes.of(board, user, read.get().likeOrHate)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  fun createBoard(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.createBoard(req.toEntity())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.getActiveBoard(boardId)

    if (userSession.unmatches(board.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.title = req.title
    board.content = req.content

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun deleteBoard(boardId: Long, userSession: UserSession) {
    val board = boardDomain.getActiveBoard(boardId)

    if (userSession.unmatches(board.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.deleted = true
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String) {
    if (boardDomain.optionalBoardHit(boardId, ip).isEmpty) {
      boardDomain.hitBoard(boardId, ip)

      val board = boardDomain.getBoard(boardId)
      board.hitCount++
    }
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String, userSession: UserSession) {
    this.hitBoard(boardId, ip)

    boardDomain.optionalBoardRead(boardId, userSession.id).orElseGet {
      boardDomain.readBoard(boardId, userSession.id)
    }
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    var read: BoardRead? = null
    boardDomain.optionalBoardRead(boardId, userSession.id).ifPresentOrElse({
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardDomain.readBoard(boardId, userSession.id, likeOrHate)
    })

    return BoardLikeRes.of(read)
  }
}