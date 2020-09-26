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

  fun findAllByCreatedByAndActiveOrderByIdDesc(
      createdBy: Long,
      active: Boolean = false,
      pageable: Pageable
  ): Page<Board>

  fun findByIdAndActive(
      id: Long?,
      active: Boolean = false
  ): Optional<Board>
}