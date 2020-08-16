package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.BoardLike
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import me.hong.kotlinspring.data.entity.board.embedded.BoardLikeId
import me.hong.kotlinspring.data.repo.board.BoardHitRepo
import me.hong.kotlinspring.data.repo.board.BoardLikeRepo
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class BoardService(
    private val boardRepo: BoardRepo,
    private val boardHitRepo: BoardHitRepo,
    private val boardLikeRepo: BoardLikeRepo,
    private val userService: UserService
) {
  fun getBoards(page: Int, size: Int): Collection<BoardRes> {
    val pageable: Pageable = PageRequest.of(page, size)
    val boards = boardRepo.findAll(pageable)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val pageable: Pageable = PageRequest.of(page, size)
    val boards = boardRepo.findAllByTitleContainingOrContentContaining(word, word, pageable)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long): Collection<BoardRes> {
    val boards = boardRepo.findAllByUserIdAndDeletedOrderByIdDesc(userId)

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )

    return BoardRes.listOf(boards, users)
  }

  fun getBoard(boardId: Long, ip: String): BoardDetailRes {
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    hitBoard(board, ip)

    val user = userService.getUser(board.userId)

    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession, ip: String): BoardDetailRes {
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    hitBoard(board, ip)

    val user = userService.getUser(board.userId)

    val like = boardLikeRepo.findByIdOrNull(BoardLikeId(boardId, board.userId))
    return if (like != null) {
      BoardDetailRes.of(board, user, like.likeOrHate)
    } else {
      BoardDetailRes.of(board, user)
    }
  }

  fun createBoard(req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.save(req.toEntity())

    return BoardPutRes.of(board, userSession)
  }

  @Transactional
  fun updateBoard(boardId: Long, req: BoardPutReq, userSession: UserSession): BoardPutRes {
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
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
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    if (userSession.unmatches(board.userId)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    board.deleted = true
  }

  @Transactional
  fun hitBoard(board: Board, ip: String): BoardHit {
    val hitId = BoardHitId(
        boardId = board.id,
        date = LocalDate.now(),
        ip = ip
    )

    val hit = boardHitRepo.findById(hitId)
    if (hit.isEmpty) {
      boardHitRepo.insert(BoardHit(hitId))
      board.hitCount++
    }
    return hit.get()
  }

  @Transactional
  fun likeOrHateBoard(boardId: Long, req: BoardLIkeReq, userSession: UserSession): BoardLikeRes {
    if (boardRepo.existsByIdAndDeleted(boardId)) {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
    val likeId = BoardLikeId(
        boardId = boardId,
        userId = userSession.id
    )

    var like: BoardLike? = null
    boardLikeRepo.findById(likeId).ifPresentOrElse({
      if (it.likeOrHate == req.likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = req.likeOrHate
      like = it
    }, {
      like = boardLikeRepo.insert(req.toEntity(likeId))
    })

    return BoardLikeRes.of(like)
  }
}