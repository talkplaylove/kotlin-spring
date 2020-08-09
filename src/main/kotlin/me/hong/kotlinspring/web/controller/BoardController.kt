package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.board.BoardReq
import me.hong.kotlinspring.web.model.board.BoardRes
import me.hong.kotlinspring.web.service.BoardService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BoardController(
    private val boardService: BoardService,
    private val userSession: UserSession
) {

  @GetMapping("/board/{boardId}")
  fun get(@PathVariable boardId: Long): BoardRes {
    return boardService.get(boardId)
  }

  @PostMapping("/board")
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody req: BoardReq): BoardRes {
    if (userSession.exists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.create(req)
  }

  @PatchMapping("/board/{boardId}")
  fun update(@PathVariable boardId: Long, @RequestBody req: BoardReq): BoardRes {
    if (userSession.exists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    return boardService.update(boardId, req)
  }

  @DeleteMapping("/board/{boardId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable boardId: Long) {
    if (userSession.exists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
    boardService.delete(boardId)
  }
}