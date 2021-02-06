package me.hong.kotlinspring.web.service.board

import me.hong.kotlinspring.data.domain.board.BoardCommentDomain
import me.hong.kotlinspring.data.domain.board.BoardCommentReadDomain
import me.hong.kotlinspring.data.domain.board.BoardUserDomain
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.enums.board.LikeOrHate
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.SigninUser
import me.hong.kotlinspring.web.model.board.BoardCommentLikeRes
import me.hong.kotlinspring.web.model.board.BoardCommentPutReq
import me.hong.kotlinspring.web.model.board.BoardCommentPutRes
import me.hong.kotlinspring.web.model.board.BoardCommentRes
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class BoardCommentService(
  private val boardCommentDomain: BoardCommentDomain,
  private val boardCommentReadDomain: BoardCommentReadDomain,
  private val boardUserDomain: BoardUserDomain
) {
  fun getComments(boardId: Long, pageable: Pageable): Collection<BoardCommentRes> {
    val comments = boardCommentDomain.getActivePage(boardId, pageable)

    val userIds = comments.stream().map { it.createdBy }.collect(Collectors.toSet())
    val users = boardUserDomain.getMap(userIds)
    return BoardCommentRes.listOf(comments.content, users)
  }

  @Transactional
  fun createComment(boardId: Long, req: BoardCommentPutReq, signinUser: SigninUser): BoardCommentPutRes {
    val comment = boardCommentDomain.save(req.toBoardComment(boardId))

    return BoardCommentPutRes.of(comment, signinUser)
  }

  @Transactional
  fun updateComment(
    boardId: Long,
    commentId: Long,
    req: BoardCommentPutReq,
    signinUser: SigninUser
  ): BoardCommentPutRes {
    val comment = boardCommentDomain.getActiveOne(commentId)

    signinUser.unmatchesThrow(comment.createdBy)

    comment.update(req.toBoardComment(boardId))

    return BoardCommentPutRes.of(comment, signinUser)
  }

  @Transactional
  fun deleteComment(boardId: Long, commentId: Long, signinUser: SigninUser) {
    val comment = boardCommentDomain.getActiveOne(commentId)

    signinUser.unmatchesThrow(comment.createdBy)

    comment.deactivate()
  }

  @Transactional
  fun likeOrHateComment(
    boardId: Long,
    commentId: Long,
    likeOrHate: LikeOrHate,
    signinUser: SigninUser
  ): BoardCommentLikeRes {
    val userId = signinUser.id
    var read: BoardCommentRead? = null
    var currentLikeOrHate = LikeOrHate.NONE

    boardCommentReadDomain.getOptional(commentId, userId).ifPresentOrElse({
      currentLikeOrHate = it.likeOrHate
      if (currentLikeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.read(likeOrHate)
      read = it
    }, {
      read = boardCommentReadDomain.create(commentId, userId, likeOrHate)
    })

    val comment = boardCommentDomain.getOne(commentId)
    when (currentLikeOrHate) {
      LikeOrHate.NONE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.LIKE -> comment.like()
          LikeOrHate.HATE -> comment.hate()
        }
      }
      LikeOrHate.LIKE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> comment.unlike()
          LikeOrHate.LIKE -> throw CustomException(CustomMessage.SAME_VALUES)
          LikeOrHate.HATE -> comment.unlikeAndHate()
        }
      }
      LikeOrHate.HATE -> {
        when (likeOrHate) {
          LikeOrHate.NONE -> comment.unhate()
          LikeOrHate.LIKE -> comment.unhateAndLike()
          LikeOrHate.HATE -> throw CustomException(CustomMessage.SAME_VALUES)
        }
      }
    }

    return BoardCommentLikeRes.of(read)
  }
}