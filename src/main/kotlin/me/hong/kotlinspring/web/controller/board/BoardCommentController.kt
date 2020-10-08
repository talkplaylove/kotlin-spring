package me.hong.kotlinspring.web.controller.board

import me.hong.kotlinspring.web.advice.SigninUser
import me.hong.kotlinspring.web.model.board.*
import me.hong.kotlinspring.web.service.board.BoardCommentService
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Validated
class BoardCommentController(
    private val boardCommentService: BoardCommentService,
    private val signinUser: SigninUser
) {
  @GetMapping("/boards/{boardId}/comments")
  fun get(@PathVariable boardId: Long,
          pageable: Pageable): Collection<BoardCommentRes> {
    return boardCommentService.getComments(boardId, pageable)
  }

  @PostMapping("/boards/{boardId}/comments")
  fun create(@PathVariable boardId: Long,
             @RequestBody @Valid req: BoardCommentPutReq): BoardCommentPutRes {
    signinUser.unexistsThrow()
    return boardCommentService.createComment(boardId, req, signinUser)
  }

  @PatchMapping("/boards/{boardId}/comments/{commentId}")
  fun update(@PathVariable boardId: Long,
             @PathVariable commentId: Long,
             @RequestBody @Valid req: BoardCommentPutReq): BoardCommentPutRes {
    signinUser.unexistsThrow()
    return boardCommentService.updateComment(boardId, commentId, req, signinUser)
  }

  @DeleteMapping("/boards/{boardId}/comments/{commentId}")
  fun delete(@PathVariable boardId: Long,
             @PathVariable commentId: Long) {
    signinUser.unexistsThrow()
    boardCommentService.deleteComment(boardId, commentId, signinUser)
  }

  @PutMapping("/boards/{boardId}/comments/{commentId}/like-or-hate")
  fun likeOrHate(@PathVariable boardId: Long,
                 @PathVariable commentId: Long,
                 @RequestBody @Valid req: BoardCommentLIkeReq): BoardCommentLikeRes {
    signinUser.unexistsThrow()
    return boardCommentService.likeOrHateComment(boardId, commentId, req.likeOrHate, signinUser)
  }
}