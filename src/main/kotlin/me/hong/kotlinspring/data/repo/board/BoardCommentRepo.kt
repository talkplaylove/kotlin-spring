package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardCommentRepo : JpaRepository<BoardComment, Long> {

  fun findAllByBoardIdAndActive(
      boardId: Long,
      active: Boolean = true,
      pageable: Pageable
  ): Page<BoardComment>

  fun findByIdAndActive(
      id: Long,
      active: Boolean = true
  ): Optional<BoardComment>
}