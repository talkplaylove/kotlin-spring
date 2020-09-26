package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardDomain(
    private val boardRepo: BoardRepo
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
    return boardRepo.findAllByCreatedByAndActiveOrderByIdDesc(
        createdBy = createdBy,
        pageable = PageRequest.of(page, size)
    )
  }

  fun getActiveBoard(boardId: Long): Board {
    return this.optionalActiveBoard(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun updateBoard(currentBoard: Board, requestBoard: Board): Board {
    currentBoard.update(requestBoard)
    return currentBoard
  }

  fun deactivateBoard(board: Board): Board {
    board.deactivate()
    return board
  }

  fun optionalActiveBoard(boardId: Long): Optional<Board> {
    return boardRepo.findByIdAndActive(boardId)
  }

  fun getBoard(boardId: Long): Board {
    return this.optionalBoard(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun optionalBoard(boardId: Long): Optional<Board> {
    return boardRepo.findById(boardId)
  }

  fun createBoard(board: Board): Board {
    return boardRepo.save(board)
  }

  fun countLikeOrHateBoard(boardId: Long, currentLikeOrHate: LikeOrHate, requestLikeOrHate: LikeOrHate): Board {
    val board = this.getBoard(boardId)
    when (currentLikeOrHate) {
      LikeOrHate.NONE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.LIKE -> board.like()
          LikeOrHate.HATE -> board.hate()
        }
      }
      LikeOrHate.LIKE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> board.unlike()
          LikeOrHate.LIKE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.HATE -> board.unlikeAndHate()
        }
      }
      LikeOrHate.HATE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> board.unhate()
          LikeOrHate.LIKE -> board.unhateAndLike()
          LikeOrHate.HATE -> throw CustomException(CustomMessage.SAME_VALUES)
        }
      }
    }
    return board
  }
}