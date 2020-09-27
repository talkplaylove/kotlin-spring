package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardCommentReadId
import me.hong.kotlinspring.data.repo.board.BoardCommentReadRepo
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardCommentReadDomain(
    private val boardCommentReadRepo: BoardCommentReadRepo
) {
  fun getOptional(commentId: Long, userId: Long): Optional<BoardCommentRead> {
    return boardCommentReadRepo.findById(BoardCommentReadId(
        commentId = commentId,
        userId = userId
    ))
  }

  fun read(commentId: Long, userId: Long, likeOrHate: LikeOrHate): BoardCommentRead {
    return boardCommentReadRepo.insert(BoardCommentRead(
        id = BoardCommentReadId(commentId, userId),
        likeOrHate = likeOrHate
    ))
  }
}