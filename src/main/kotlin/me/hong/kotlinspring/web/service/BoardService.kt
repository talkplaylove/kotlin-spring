package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import me.hong.kotlinspring.data.entity.board.embedded.BoardReadId
import me.hong.kotlinspring.data.repo.board.BoardHitRepo
import me.hong.kotlinspring.data.repo.board.BoardReadRepo
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class BoardService(
    private val boardRepo: BoardRepo,
    private val boardReadRepo: BoardReadRepo,
    private val boardHitRepo: BoardHitRepo,
    private val userService: UserService
) {
  fun boards(page: Int, size: Int): Collection<BoardRes> {
    val boards = boardRepo.findAll(PageRequest.of(page, size))

    val users = userService.getUsers(
        ids = boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun searchBoards(word: String, page: Int, size: Int): Collection<BoardRes> {
    if (word.length == 1)
      throw CustomException(CustomMessage.AT_LEAST_TWO_LETTERS)

    val boards = boardRepo.findAllByTitleContainingOrContentContaining(
        title = word,
        content = word,
        pageable = PageRequest.of(page, size)
    )

    val users = userService.getUsers(
        ids = boards.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardRes.listOf(boards.content, users)
  }

  fun getBoards(userId: Long, page: Int, size: Int): Collection<BoardRes> {
    val boards = boardRepo.findAllByUserIdAndDeletedOrderByIdDesc(
        userId = userId,
        pageable = PageRequest.of(page, size)
    )

    val users = userService.getUsers(
        boards.stream().map { it.userId }.collect(Collectors.toSet())
    )

    return BoardRes.listOf(boards.content, users)
  }

  fun getBoard(boardId: Long): BoardDetailRes {
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    val user = userService.getUser(board.userId)

    return BoardDetailRes.of(board, user)
  }

  fun getBoard(boardId: Long, userSession: UserSession): BoardDetailRes {
    val board = boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }

    val user = userService.getUser(board.userId)

    val read = boardReadRepo.findById(BoardReadId(
        boardId = boardId,
        userId = userSession.id
    ))
    return if (read.isPresent) {
      BoardDetailRes.of(board, read.get().likeOrHate, user)
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
  fun hitBoard(boardId: Long, ip: String) {
    val hitId = BoardHitId(
        boardId = boardId,
        date = LocalDate.now(),
        ip = ip
    )

    boardHitRepo.save(BoardHit(hitId))
  }

  @Transactional
  fun hitBoard(boardId: Long, ip: String, userSession: UserSession) {
    hitBoard(boardId, ip)

    val readId = BoardReadId(
        boardId = boardId,
        userId = userSession.id
    )
    if (!boardReadRepo.existsById(readId)) {
      boardReadRepo.insert(BoardRead(readId, LikeOrHate.NONE))
    }
  }

  @Transactional
  fun readBoard(boardId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardLikeRes {
    var read: BoardRead? = null
    val readId = BoardReadId(boardId, userSession.id)

    boardReadRepo.findById(readId).ifPresentOrElse({
      if (it.likeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardReadRepo.insert(BoardRead(
          id = readId,
          likeOrHate = likeOrHate
      ))
    })

    return BoardLikeRes.of(read)
  }
}