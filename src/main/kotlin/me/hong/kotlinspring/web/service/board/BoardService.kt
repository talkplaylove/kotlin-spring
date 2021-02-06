package me.hong.kotlinspring.web.service.board

import me.hong.kotlinspring.data.domain.board.BoardDomain
import me.hong.kotlinspring.data.domain.board.BoardHitDomain
import me.hong.kotlinspring.data.domain.board.BoardReadDomain
import me.hong.kotlinspring.data.domain.board.BoardUserDomain
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.enums.board.LikeOrHate
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.SigninUser
import me.hong.kotlinspring.web.model.board.*
import org.springframework.data.domain.Pageable
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
  fun getBoards(pageable: Pageable): Collection<BoardRes> {
    val boards = boardDomain.getActivePage(pageable)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, pageable: Pageable): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardDomain.getActivePage(word, pageable)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, pageable: Pageable): Collection<BoardRes> {
    val boards = boardDomain.getActivePage(userId, pageable)

    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long): BoardDetailRes {
    val board = boardDomain.getActiveOne(boardId)

    val user = boardUserDomain.getOneNullable(board.createdBy)
    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, signinUser: SigninUser): BoardDetailRes {
    val userId = signinUser.id

    val board = boardDomain.getActiveOne(boardId)
    val user = boardUserDomain.getOneNullable(board.createdBy)
    val read = boardReadDomain.getOptional(boardId, userId)

    return if (read.isPresent) {
      BoardDetailRes.of(board, user, read.get().likeOrHate)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  @Transactional
  fun createBoard(req: BoardPutReq, signinUser: SigninUser): BoardPutRes {
    val board = boardDomain.save(req.toBoard())

    return BoardPutRes.of(board, signinUser)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, signinUser: SigninUser): BoardPutRes {
    val board = boardDomain.getActiveOne(boardId)

    signinUser.unmatchesThrow(board.createdBy)

    board.update(req.toBoard())

    return BoardPutRes.of(board, signinUser)
  }

  @Transactional
  fun deleteBoard(boardId: Long, signinUser: SigninUser) {
    val board = boardDomain.getActiveOne(boardId)

    signinUser.unmatchesThrow(board.createdBy)

    board.deactivate()
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String) {
    if (boardHitDomain.getOptional(boardId, ip).isEmpty) {
      boardHitDomain.create(boardId, ip)

      val board = boardDomain.getOne(boardId)
      board.hit()
    }
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String, signinUser: SigninUser) {
    val userId = signinUser.id
    this.hitBoard(boardId, ip)

    boardReadDomain.getOptional(boardId, userId).orElseGet {
      boardReadDomain.create(boardId, userId)
    }
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, signinUser: SigninUser): BoardLikeRes {
    val userId = signinUser.id
    var read: BoardRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardReadDomain.getOptional(boardId, userId).ifPresentOrElse({
      currentLikeOrHate = it.likeOrHate
      if (currentLikeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.read(likeOrHate)
      read = it
    }, {
      read = boardReadDomain.create(boardId, userId, likeOrHate)
    })

    val board = boardDomain.getOne(boardId)
    when (currentLikeOrHate) {
      LikeOrHate.NONE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.LIKE -> board.like()
          LikeOrHate.HATE -> board.hate()
        }
      }
      LikeOrHate.LIKE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> board.unlike()
          LikeOrHate.LIKE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.HATE -> board.unlikeAndHate()
        }
      }
      LikeOrHate.HATE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> board.unhate()
          LikeOrHate.LIKE -> board.unhateAndLike()
          LikeOrHate.HATE -> throw CustomException(CustomMessage.SAME_VALUES)
        }
      }
    }

    return BoardLikeRes.of(read)
  }

  fun createBoardUser(userId: Long, userName: String) {
    if (boardUserDomain.getOptional(userId).isEmpty) {
      boardUserDomain.create(BoardUser(userId, userName))
    }
  }

  fun updateBoardUser(userId: Long, userName: String) {
    boardUserDomain.getOptional(userId).ifPresent {
      it.update(userName)
    }
  }
}