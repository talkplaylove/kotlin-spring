package me.hong.kotlinspring.web.domain

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import me.hong.kotlinspring.data.entity.board.embedded.BoardReadId
import me.hong.kotlinspring.data.repo.board.BoardHitRepo
import me.hong.kotlinspring.data.repo.board.BoardReadRepo
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class BoardDomain(
    private val boardRepo: BoardRepo,
    private val boardReadRepo: BoardReadRepo,
    private val boardHitRepo: BoardHitRepo
) {
  fun findBoards(page: Int, size: Int): Page<Board> {
    return boardRepo.findAll(PageRequest.of(page, size))
  }

  fun findBoards(word: String, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByTitleContainingOrContentContaining(
        title = word,
        content = word,
        pageable = PageRequest.of(page, size)
    )
  }

  fun findBoards(createdBy: Long, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByCreatedByAndDeletedOrderByIdDesc(
        createdBy = createdBy,
        pageable = PageRequest.of(page, size)
    )
  }

  fun getActiveBoard(boardId: Long): Board {
    return boardRepo.findByIdAndDeleted(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun getBoard(boardId: Long): Board {
    return boardRepo.findById(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun optionalBoardRead(boardId: Long, userId: Long): Optional<BoardRead> {
    return boardReadRepo.findById(BoardReadId(
        boardId = boardId,
        userId = userId
    ))
  }

  fun optionalBoardHit(boardId: Long, ip: String): Optional<BoardHit> {
    return boardHitRepo.findById(
        BoardHitId(
            boardId = boardId,
            date = LocalDate.now(),
            ip = ip
        )
    )
  }

  fun createBoard(board: Board): Board {
    return boardRepo.save(board)
  }

  fun hitBoard(boardId: Long, ip: String): BoardHit {
    return boardHitRepo.insert(BoardHit(
        BoardHitId(
            boardId = boardId,
            date = LocalDate.now(),
            ip = ip
        )
    ))
  }

  fun readBoard(boardId: Long, userId: Long): BoardRead {
    return this.readBoard(boardId, userId, LikeOrHate.NONE)
  }

  fun readBoard(boardId: Long, userId: Long, likeOrHate: LikeOrHate): BoardRead {
    return boardReadRepo.insert(BoardRead(
        id = BoardReadId(
            boardId = boardId,
            userId = userId
        ),
        likeOrHate = likeOrHate
    ))
  }
}