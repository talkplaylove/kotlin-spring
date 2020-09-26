package me.hong.kotlinspring.data.domain

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

  fun optionalComment(commentId: Long): Optional<BoardComment> {
    return boardCommentRepo.findById(commentId)
  }

  fun getComment(commentId: Long): BoardComment {
    return this.optionalComment(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }
  }

  fun optionalActiveComment(commentId: Long): Optional<BoardComment> {
    return this.boardCommentRepo.findByIdAndDeleted(commentId)
  }

  fun getActiveComment(commentId: Long): BoardComment {
    return this.optionalActiveComment(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }
  }

  fun updateComment(currentComment: BoardComment, requestComment: BoardComment): BoardComment {
    currentComment.update(requestComment)
    return currentComment
  }

  fun deactivateComment(comment: BoardComment): BoardComment {
    comment.deactivate()
    return comment
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

  fun countLikeOrHateComment(commentId: Long, currentLikeOrHate: LikeOrHate, requestLikeOrHate: LikeOrHate): BoardComment {
    val comment = this.getComment(commentId)
    when (currentLikeOrHate) {
      LikeOrHate.NONE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.LIKE -> comment.like()
          LikeOrHate.HATE -> comment.hate()
        }
      }
      LikeOrHate.LIKE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> comment.unlike()
          LikeOrHate.LIKE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.HATE -> comment.unlikeAndHate()
        }
      }
      LikeOrHate.HATE -> {
        when (requestLikeOrHate) {
          LikeOrHate.NONE -> comment.unhate()
          LikeOrHate.LIKE -> comment.unhateAndLike()
          LikeOrHate.HATE -> throw CustomException(CustomMessage.SAME_VALUES)
        }
      }
    }
    return comment
  }
}