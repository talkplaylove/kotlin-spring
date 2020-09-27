package me.hong.kotlinspring.web.service.board

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.domain.board.BoardCommentDomain
import me.hong.kotlinspring.data.domain.board.BoardCommentReadDomain
import me.hong.kotlinspring.data.domain.board.BoardUserDomain
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardCommentLikeRes
import me.hong.kotlinspring.web.model.board.BoardCommentPutReq
import me.hong.kotlinspring.web.model.board.BoardCommentPutRes
import me.hong.kotlinspring.web.model.board.BoardCommentRes
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class BoardCommentService(
    private val boardCommentDomain: BoardCommentDomain,
    private val boardCommentReadDomain: BoardCommentReadDomain,
    private val boardUserDomain: BoardUserDomain
) {
  fun getComments(boardId: Long, page: Int, size: Int): Collection<BoardCommentRes> {
    val comments = boardCommentDomain.getActivePage(boardId, page, size)

    val userIds = comments.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardCommentRes.listOf(comments.content, users)
  }

  @Transactional
  fun createComment(boardId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentDomain.create(req.toBoardComment(boardId))

    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun updateComment(boardId: Long, commentId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentDomain.getActiveOne(commentId)

    userSession.unmatchesThrow(comment.createdBy)

    boardCommentDomain.update(comment, req.toBoardComment(boardId))

    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun deleteComment(boardId: Long, commentId: Long, userSession: UserSession) {
    val comment = boardCommentDomain.getActiveOne(commentId)

    userSession.unmatchesThrow(comment.createdBy)

    boardCommentDomain.deactivate(comment)
  }

  @Transactional
  fun likeOrHateComment(boardId: Long, commentId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardCommentLikeRes {
    val userId = userSession.id
    var read: BoardCommentRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardCommentReadDomain.getOptional(commentId, userId).ifPresentOrElse({
      currentLikeOrHate = it.likeOrHate
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardCommentReadDomain.read(commentId, userId, likeOrHate)
    })

    if (currentLikeOrHate == likeOrHate) {
      throw CustomException(CustomMessage.SAME_VALUES)
    }

    boardCommentDomain.countLikeOrHate(commentId, currentLikeOrHate, likeOrHate)

    return BoardCommentLikeRes.of(read)
  }
}