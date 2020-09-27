package me.hong.kotlinspring.web.controller.board

import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import me.hong.kotlinspring.web.service.board.BoardCommentService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@Validated
class BoardCommentController(
    private val boardCommentService: BoardCommentService,
    private val userSession: UserSession
) {
  @GetMapping("/boards/{boardId}/comments")
  fun get(@PathVariable boardId: Long,
          @RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardCommentRes> {
    return boardCommentService.getComments(boardId, page, size)
  }

  @PostMapping("/boards/{boardId}/comments")
  fun create(@PathVariable boardId: Long,
             @RequestBody @Valid req: BoardCommentPutReq): BoardCommentPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardCommentService.createComment(boardId, req, userSession)
  }

  @PatchMapping("/boards/{boardId}/comments/{commentId}")
  fun update(@PathVariable boardId: Long,
             @PathVariable commentId: Long,
             @RequestBody @Valid req: BoardCommentPutReq): BoardCommentPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardCommentService.updateComment(boardId, commentId, req, userSession)
  }

  @DeleteMapping("/boards/{boardId}/comments/{commentId}")
  fun delete(@PathVariable boardId: Long,
             @PathVariable commentId: Long) {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    boardCommentService.deleteComment(boardId, commentId, userSession)
  }

  @PutMapping("/boards/{boardId}/comments/{commentId}/like-or-hate")
  fun likeOrHate(@PathVariable boardId: Long,
                 @PathVariable commentId: Long,
                 @RequestBody @Valid req: BoardCommentLIkeReq): BoardCommentLikeRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardCommentService.likeOrHateComment(boardId, commentId, req.likeOrHate, userSession)
  }
}