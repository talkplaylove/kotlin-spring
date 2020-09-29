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
}