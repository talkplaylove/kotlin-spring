package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.domain.BoardCommentDomain
import me.hong.kotlinspring.web.domain.BoardUserDomain
import me.hong.kotlinspring.web.model.board.BoardCommentLikeRes
import me.hong.kotlinspring.web.model.board.BoardCommentPutReq
import me.hong.kotlinspring.web.model.board.BoardCommentPutRes
import me.hong.kotlinspring.web.model.board.BoardCommentRes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardCommentService(
    private val boardCommentDomain: BoardCommentDomain,
    private val boardUserDomain: BoardUserDomain
) {
  fun getComments(boardId: Long, page: Int, size: Int): Collection<BoardCommentRes> {
    val comments = boardCommentDomain.findComments(boardId, page, size)

    val users = boardUserDomain.getCommentUsers(comments)
    return BoardCommentRes.listOf(comments.content, users)
  }

  @Transactional
  fun createComment(boardId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentDomain.createComment(req.toBoardComment(boardId))

    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun updateComment(boardId: Long, commentId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentDomain.getActiveComment(commentId)

    if (userSession.unmatches(comment.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    comment.content = req.content
    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun deleteComment(boardId: Long, commentId: Long, userSession: UserSession) {
    val comment = boardCommentDomain.getActiveComment(commentId)

    if (userSession.unmatches(comment.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    comment.deleted = true
  }

  @Transactional
  fun likeOrHateComment(boardId: Long, commentId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardCommentLikeRes {
    val userId = userSession.id
    var read: BoardCommentRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardCommentDomain.optionalCommentRead(commentId, userId).ifPresentOrElse({
      currentLikeOrHate = it.likeOrHate
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardCommentDomain.readComment(commentId, userId, likeOrHate)
    })

    if (currentLikeOrHate == likeOrHate) {
      throw CustomException(CustomMessage.SAME_VALUES)
    }

    boardCommentDomain.countLikeOrHateComment(commentId, currentLikeOrHate, likeOrHate)

    return BoardCommentLikeRes.of(read)
  }
}