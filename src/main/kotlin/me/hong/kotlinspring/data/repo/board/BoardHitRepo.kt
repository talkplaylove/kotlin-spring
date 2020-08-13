package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import org.springframework.data.jpa.repository.JpaRepository

interface BoardHitRepo : JpaRepository<BoardHit, BoardHitId> {
}