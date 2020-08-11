package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardPutReq
import me.hong.kotlinspring.web.model.board.BoardPutRes
import me.hong.kotlinspring.web.model.board.BoardDetailRes
import me.hong.kotlinspring.web.service.BoardService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    private val boardService: BoardService,
    private val userSession: UserSession
) {

  @GetMapping("/board/{boardId}")
  fun get(@PathVariable boardId: Long): BoardDetailRes {
    return boardService.get(boardId)
  }

  @PostMapping("/board")
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody req: BoardPutReq): BoardPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.create(req, userSession)
  }

  @PatchMapping("/board/{boardId}")
  fun update(@PathVariable boardId: Long, @RequestBody req: BoardPutReq): BoardPutRes {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.update(boardId, req, userSession)
  }

  @DeleteMapping("/board/{boardId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable boardId: Long) {
    if (userSession.unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    boardService.delete(boardId, userSession)
  }
}