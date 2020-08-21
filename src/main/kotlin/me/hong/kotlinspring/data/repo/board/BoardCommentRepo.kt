package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardCommentRepo : JpaRepository<BoardComment, Long> {

  fun findAllByBoardIdAndDeleted(
      boardId: Long,
      pageable: Pageable,
      deleted: Boolean = false
  ): Page<BoardComment>

  fun findByIdAndDeleted(
      id: Long,
      deleted: Boolean = false
  ): Optional<BoardComment>
}