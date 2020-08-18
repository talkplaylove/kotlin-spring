package me.hong.kotlinspring.data.domain

import me.hong.kotlinspring.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardReadId
import me.hong.kotlinspring.data.repo.board.BoardReadRepo
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class BoardReadDomain(
    private val boardReadRepo: BoardReadRepo
) {
  fun getRead(boardId: Long, userId: Long): Optional<BoardRead> {
    return boardReadRepo.findById(BoardReadId(boardId, userId))
  }

  @Transactional
  fun likeOrHate(boardId: Long, userId: Long, likeOrHate: LikeOrHate): BoardRead {
    return boardReadRepo.insert(BoardRead(
        id = BoardReadId(boardId, userId),
        likeOrHate = likeOrHate
    ))
  }
}