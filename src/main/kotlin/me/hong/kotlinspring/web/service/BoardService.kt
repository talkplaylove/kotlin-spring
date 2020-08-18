package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.domain.BoardDomain
import me.hong.kotlinspring.data.domain.BoardHitDomain
import me.hong.kotlinspring.data.domain.BoardReadDomain
import me.hong.kotlinspring.data.entity.board.BoardRead
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
    private val boardReadDomain: BoardReadDomain,
    private val boardHitDomain: BoardHitDomain,
    private val userService: UserService
) {
  fun boards(page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.getBoards(page, size)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardDomain.searchBoards(word, page, size)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, page: Int, size: Int): Collection<BoardRes> {
    val boards = boardDomain.getBoards(userId, page, size)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )

    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long, ip: String): BoardDetailRes {
    val board = boardDomain.getBoard(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    boardHitDomain.hit(board, ip)

    val user = userService.getUser(board.userId)

    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession, ip: String): BoardDetailRes {
    val board = boardDomain.getBoard(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    boardHitDomain.hit(board, ip)
    readBoard(boardId, LikeOrHate.NONE, userSession)

    val user = userService.getUser(board.userId)

    val read = boardReadDomain.getRead(boardId, userSession.id)
    return if (read.isPresent) {
      BoardDetailRes.of(board, read.get().likeOrHate, user)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  fun createBoard(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.saveBoard(req.toEntity())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardDomain.getBoard(boardId).orElseThrow {
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
  fun deleteBoard(boardId: Long, userSession: UserSession) {
    val board = boardDomain.getBoard(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.deleted = true
  }

  fun likeOrHateBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    if (!boardDomain.existsBoard(boardId)) {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    return readBoard(boardId, likeOrHate, userSession)
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    var read: BoardRead? = null
    boardReadDomain.getRead(boardId, userSession.id).ifPresentOrElse({
      if (it.likeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardReadDomain.likeOrHate(boardId, userSession.id, likeOrHate)
    })

    return BoardLikeRes.of(read)
  }
}