package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.constant.board.LikeOrHate
import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardCommentReadId
import me.hong.kotlinspring.data.repo.board.BoardCommentReadRepo
import me.hong.kotlinspring.data.repo.board.BoardCommentRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardCommentLikeRes
import me.hong.kotlinspring.web.model.board.BoardCommentPutReq
import me.hong.kotlinspring.web.model.board.BoardCommentPutRes
import me.hong.kotlinspring.web.model.board.BoardCommentRes
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class BoardCommentService(
    private val boardCommentRepo: BoardCommentRepo,
    private val boardCommentReadRepo: BoardCommentReadRepo,
    private val userService: UserService
) {
  fun getComments(boardId: Long, page: Int, size: Int): Collection<BoardCommentRes> {
    val comments = boardCommentRepo.findAllByBoardIdAndDeleted(boardId, PageRequest.of(page, size))

    val users = userService.getUsers(
        ids = comments.stream().map { it.createdBy }.collect(Collectors.toSet())
    )
    return BoardCommentRes.listOf(comments.content, users)
  }

  fun createComment(boardId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentRepo.save(req.toEntity(boardId))

    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun updateComment(boardId: Long, commentId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentRepo.findByIdAndDeleted(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }

    if (userSession.unmatches(comment.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    comment.content = req.content
    return BoardCommentPutRes.of(comment, userSession)
  }

  @Transactional
  fun deleteComment(boardId: Long, commentId: Long, userSession: UserSession) {
    val comment = boardCommentRepo.findByIdAndDeleted(commentId).orElseThrow {
      throw CustomException(CustomMessage.COMMENT_NOT_FOUND)
    }

    if (userSession.unmatches(comment.createdBy)) {
      throw CustomException(CustomMessage.FORBIDDEN)
    }

    comment.deleted = true
  }

  @Transactional
  fun likeOrHateComment(boardId: Long, commentId: Long, likeOrHate: LikeOrHate, userSession: UserSession): BoardCommentLikeRes {
    val userId = userSession.id
    var read: BoardCommentRead? = null
    val readId = BoardCommentReadId(commentId, userId)

    boardCommentReadRepo.findById(readId).ifPresentOrElse({
      if (it.likeOrHate == likeOrHate) {
        throw CustomException(CustomMessage.SAME_VALUES)
      }
      it.likeOrHate = likeOrHate
      read = it
    }, {
      read = boardCommentReadRepo.insert(BoardCommentRead(
          id = readId,
          likeOrHate = likeOrHate
      ))
    })

    return BoardCommentLikeRes.of(read)
  }
}