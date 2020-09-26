package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardReadId
import me.hong.kotlinspring.data.repo.board.BoardReadRepo
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardReadDomain(
    private val boardReadRepo: BoardReadRepo
) {
  fun optional(boardId: Long, userId: Long): Optional<BoardRead> {
    return boardReadRepo.findById(BoardReadId(
        boardId = boardId,
        userId = userId
    ))
  }

  fun read(boardId: Long, userId: Long): BoardRead {
    return this.read(boardId, userId, LikeOrHate.NONE)
  }

  fun read(boardId: Long, userId: Long, likeOrHate: LikeOrHate): BoardRead {
    return boardReadRepo.insert(BoardRead(
        id = BoardReadId(
            boardId = boardId,
            userId = userId
        ),
        likeOrHate = likeOrHate
    ))
  }
}