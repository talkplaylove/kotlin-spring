package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentRepo : JpaRepository<BoardComment, Long> {

  fun findAllByBoardId(boardId: Long, pageable: Pageable): Page<BoardComment>
}