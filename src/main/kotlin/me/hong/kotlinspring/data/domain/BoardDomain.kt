package me.hong.kotlinspring.data.domain

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.repo.board.BoardRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardDomain(
    private val boardRepo: BoardRepo
) {
  fun getBoard(id: Long): Optional<Board> {
    return boardRepo.findByIdAndDeleted(id)
  }

  fun existsBoard(id: Long): Boolean {
    return boardRepo.existsByIdAndDeleted(id)
  }

  fun getBoards(page: Int, size: Int): Page<Board> {
    return boardRepo.findAll(PageRequest.of(page, size))
  }

  fun searchBoards(word: String, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByTitleContainingOrContentContaining(
        title = word,
        content = word,
        pageable = PageRequest.of(page, size)
    )
  }

  fun getBoards(userId: Long, page: Int, size: Int): Page<Board> {
    return boardRepo.findAllByUserIdAndDeletedOrderByIdDesc(
        userId = userId,
        pageable = PageRequest.of(page, size))
  }

  fun saveBoard(board: Board): Board {
    return boardRepo.save(board)
  }
}