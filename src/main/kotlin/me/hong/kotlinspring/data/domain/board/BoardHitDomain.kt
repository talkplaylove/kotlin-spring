package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.entity.board.BoardHit
import me.hong.kotlinspring.data.entity.board.embedded.BoardHitId
import me.hong.kotlinspring.data.repo.board.BoardHitRepo
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class BoardHitDomain(
    private val boardHitRepo: BoardHitRepo
) {
  fun optional(boardId: Long, ip: String): Optional<BoardHit> {
    return boardHitRepo.findById(
        BoardHitId(
            boardId = boardId,
            date = LocalDate.now(),
            ip = ip
        )
    )
  }

  fun hit(boardId: Long, ip: String): BoardHit {
    return boardHitRepo.insert(BoardHit(
        BoardHitId(
            boardId = boardId,
            date = LocalDate.now(),
            ip = ip
        )
    ))
  }
}