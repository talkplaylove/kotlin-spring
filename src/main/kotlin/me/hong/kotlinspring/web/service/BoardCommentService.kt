package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.repo.board.BoardCommentRepo
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardCommentPutReq
import me.hong.kotlinspring.web.model.board.BoardCommentPutRes
import me.hong.kotlinspring.web.model.board.BoardCommentRes
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class BoardCommentService(
    private val boardCommentRepo: BoardCommentRepo,
    private val userService: UserService
) {
  fun getComments(boardId: Long, page: Int, size: Int): Collection<BoardCommentRes> {
    val comments = boardCommentRepo.findAllByBoardId(boardId, PageRequest.of(page, size))

    val users = userService.getUsers(
        ids = comments.stream().map { it.userId }.collect(Collectors.toSet())
    )
    return BoardCommentRes.listOf(comments.content, users)
  }

  fun createComment(boardId: Long, req: BoardCommentPutReq, userSession: UserSession): BoardCommentPutRes {
    val comment = boardCommentRepo.save(req.toEntity(boardId))

    return BoardCommentPutRes.of(comment, userSession)
  }
}