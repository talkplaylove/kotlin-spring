package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.util.ServletUtils
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.*
import me.hong.kotlinspring.web.service.BoardService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@Validated
class BoardController(
    private val boardService: BoardService,
    private val userSession: UserSession
) {
  @GetMapping("/boards")
  fun get(@RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.boards(page, size)
  }

  @GetMapping("/boards/search")
  fun get(@RequestParam word: String,
          @RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.searchBoards(word.trim(), page, size)
  }

  @GetMapping("/boards/{boardId}")
  fun get(@PathVariable boardId: Long): BoardDetailRes {
    return if (userSession.unexists())
      boardService.getBoard(boardId)
    else
      boardService.getBoard(boardId, userSession)
  }

  @GetMapping("/users/{userId}/boards")
  fun get(@PathVariable userId: Long,
          @RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.getBoards(userId, page, size)
  }

  @PostMapping("/boards")
  fun create(@RequestBody @Valid req: BoardPutReq): BoardPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.createBoard(req, userSession)
  }

  @PatchMapping("/boards/{boardId}")
  fun update(@PathVariable boardId: Long,
             @RequestBody @Valid req: BoardPutReq): BoardPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.updateBoard(boardId, req, userSession)
  }

  @DeleteMapping("/boards/{boardId}")
  fun delete(@PathVariable boardId: Long) {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    boardService.deleteBoard(boardId, userSession)
  }

  @PostMapping("/boards/{boardId}/hit")
  fun hit(@PathVariable boardId: Long, request: HttpServletRequest) {
    val ip = ServletUtils.getIp(request)

    if (userSession.unexists())
      boardService.hitBoard(boardId, ip)
    else
      boardService.hitBoard(boardId, ip, userSession)
  }

  @PutMapping("/boards/{boardId}/like-or-hate")
  fun likeOrHate(@PathVariable boardId: Long,
                 @RequestBody @Valid req: BoardLIkeReq): BoardLikeRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.readBoard(boardId, req.likeOrHate, userSession)
  }
}