package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardRepo : JpaRepository<Board, Long> {
  fun findAllByTitleContainingOrContentContaining(
      title: String?,
      content: String?,
      pageable: Pageable
  ): Page<Board>

  fun findAllByUserIdAndDeletedOrderByIdDesc(
      userId: Long,
      deleted: Boolean = false,
      pageable: Pageable
  ): Page<Board>

  fun findByIdAndDeleted(
      id: Long?,
      deleted: Boolean = false
  ): Optional<Board>
}