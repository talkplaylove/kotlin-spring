package me.hong.kotlinspring.data.domain

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import me.hong.kotlinspring.data.repo.board.BoardHitRepo
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class BoardHitDomain(
    private val boardHitRepo: BoardHitRepo
) {
  @Transactional
  fun hit(board: Board, ip: String) {
    val hitId = BoardHitId(
        boardId = board.id,
        date = LocalDate.now(),
        ip = ip
    )

    val hit = boardHitRepo.findById(hitId)
    if (hit.isEmpty) {
      boardHitRepo.insert(BoardHit(hitId))
      board.hitCount++
    }
  }
}