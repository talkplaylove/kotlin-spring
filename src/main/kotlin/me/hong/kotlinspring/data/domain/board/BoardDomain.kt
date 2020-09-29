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
  fun getActivePage(page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByActive(PageRequest.of(page, size))
  }

  fun getActivePage(word: String, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByTitleContainingOrContentContainingAndActive(
        title = word,
        content = word,
        pageable = PageRequest.of(page, size)
    )
  }

  fun getActivePage(createdBy: Long, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByCreatedByAndActiveOrderByIdDesc(
        createdBy = createdBy,
        pageable = PageRequest.of(page, size)
    )
  }

  fun getActiveOptional(boardId: Long): Optional<Board> {
    return boardRepo.findByIdAndActive(boardId)
  }

  fun getActiveOne(boardId: Long): Board {
    return this.getActiveOptional(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun getOptional(boardId: Long): Optional<Board> {
    return boardRepo.findById(boardId)
  }

  fun getOne(boardId: Long): Board {
    return this.getOptional(boardId).orElseThrow {
      throw CustomException(CustomMessage.BOARD_NOT_FOUND)
    }
  }

  fun create(board: Board): Board {
    return boardRepo.save(board)
  }
}