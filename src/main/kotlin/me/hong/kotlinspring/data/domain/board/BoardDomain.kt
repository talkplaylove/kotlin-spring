package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.repo.board.BoardRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardDomain(
  private val boardRepo: BoardRepo
) {
  fun getActivePage(pageable: Pageable): Page<Board> {
    return boardRepo.findAllByActive(pageable)
  }

  fun getActivePage(word: String, pageable: Pageable): Page<Board> {
    return boardRepo.findAllByTitleContainingOrContentContainingAndActive(
      title = word,
      content = word,
      pageable = pageable
    )
  }

  fun getActivePage(createdBy: Long, pageable: Pageable): Page<Board> {
    return boardRepo.findAllByCreatedByAndActiveOrderByIdDesc(
      createdBy = createdBy,
      pageable = pageable
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

  fun save(board: Board): Board {
    return boardRepo.save(board)
  }
}