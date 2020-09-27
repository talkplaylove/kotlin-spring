package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.data.repo.board.BoardCommentRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class BoardCommentDomain(
    private val boardCommentRepo: BoardCommentRepo
) {
  fun getActivePage(boardId: Long, page: Int, size: Int): Page<BoardComment> {
    return boardCommentRepo.findAllByBoardIdAndActive(
        boardId = boardId, pageable = PageRequest.of(page, size)
    )
  }

  fun create(comment: BoardComment): BoardComment {
    return boardCommentRepo.save(comment)
  }

  fun getOptional(commentId: Long): Optional<BoardComment> {
    return boardCommentRepo.findById(commentId)
  }

  fun getOne(commentId: Long): BoardComment {
    return this.getOptional(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }
  }

  fun getActiveOptional(commentId: Long): Optional<BoardComment> {
    return this.boardCommentRepo.findByIdAndActive(commentId)
  }

  fun getActiveOne(commentId: Long): BoardComment {
    return this.getActiveOptional(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }
  }

  fun update(currentComment: BoardComment, requestComment: BoardComment): BoardComment {
    currentComment.update(requestComment)
    return currentComment
  }

  fun deactivate(comment: BoardComment): BoardComment {
    comment.deactivate()
    return comment
  }

  fun countLikeOrHate(commentId: Long, currentLikeOrHate: LikeOrHate, requestLikeOrHate: LikeOrHate): BoardComment {
    val comment = this.getOne(commentId)
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