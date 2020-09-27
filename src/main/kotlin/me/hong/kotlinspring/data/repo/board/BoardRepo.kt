package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BoardRepo : JpaRepository<Board, Long> {
  fun findAllByActive(
      pageable: Pageable,
      active: Boolean = true
  ): Page<Board>

  fun findAllByTitleContainingOrContentContainingAndActive(
      pageable: Pageable,
      title: String?,
      content: String?,
      active: Boolean = true
  ): Page<Board>

  fun findAllByCreatedByAndActiveOrderByIdDesc(
      pageable: Pageable,
      createdBy: Long,
      active: Boolean = true
  ): Page<Board>

  fun findByIdAndActive(
      id: Long?,
      active: Boolean = true
  ): Optional<Board>
}