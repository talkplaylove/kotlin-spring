package me.hong.kotlinspring.web.domain

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardCommentReadId
import me.hong.kotlinspring.data.repo.board.BoardCommentReadRepo
import me.hong.kotlinspring.data.repo.board.BoardCommentRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardCommentDomain(
    private val boardCommentRepo: BoardCommentRepo,
    private val boardCommentReadRepo: BoardCommentReadRepo
) {
  fun findComments(boardId: Long, page: Int, size: Int): Page<BoardComment> {
    return boardCommentRepo.findAllByBoardIdAndDeleted(boardId, PageRequest.of(page, size))
  }

  fun createComment(comment: BoardComment): BoardComment {
    return boardCommentRepo.save(comment)
  }

  fun getActiveComment(commentId: Long): BoardComment {
    return boardCommentRepo.findByIdAndDeleted(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }
  }

  fun optionalCommentRead(commentId: Long, userId: Long): Optional<BoardCommentRead> {
    return boardCommentReadRepo.findById(BoardCommentReadId(
        commentId = commentId,
        userId = userId
    ))
  }

  fun readComment(commentId: Long, userId: Long, likeOrHate: LikeOrHate): BoardCommentRead {
    return boardCommentReadRepo.insert(BoardCommentRead(
        id = BoardCommentReadId(commentId, userId),
        likeOrHate = likeOrHate
    ))
  }
}