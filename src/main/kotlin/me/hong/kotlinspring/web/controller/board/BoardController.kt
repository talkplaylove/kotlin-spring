package me.hong.kotlinspring.web.controller.board

import me.hong.kotlinspring.util.ServletUtils
import me.hong.kotlinspring.web.advice.SigninUser
import me.hong.kotlinspring.web.model.board.*
import me.hong.kotlinspring.web.service.board.BoardService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@Validated
class BoardController(
    private val boardService: BoardService,
    private val signinUser: SigninUser
) {
  @GetMapping("/boards")
  fun get(@RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.getBoards(page, size)
  }

  @GetMapping("/boards/search")
  fun get(@RequestParam word: String,
          @RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.searchBoards(word.trim(), page, size)
  }

  @GetMapping("/boards/{boardId}")
  fun get(@PathVariable boardId: Long): BoardDetailRes {
    return if (signinUser.unexists())
      boardService.getBoard(boardId)
    else
      boardService.getBoard(boardId, signinUser)
  }

  @GetMapping("/users/{userId}/boards")
  fun get(@PathVariable userId: Long,
          @RequestParam(defaultValue = "0") page: Int,
          @RequestParam(defaultValue = "20") @Min(5) size: Int): Collection<BoardRes> {
    return boardService.getBoards(userId, page, size)
  }

  @PostMapping("/boards")
  fun create(@RequestBody @Valid req: BoardPutReq): BoardPutRes {
    signinUser.unexistsThrow()
    return boardService.createBoard(req, signinUser)
  }

  @PatchMapping("/boards/{boardId}")
  fun update(@PathVariable boardId: Long,
             @RequestBody @Valid req: BoardPutReq): BoardPutRes {
    signinUser.unexistsThrow()
    return boardService.updateBoard(boardId, req, signinUser)
  }

  @DeleteMapping("/boards/{boardId}")
  fun delete(@PathVariable boardId: Long) {
    signinUser.unexistsThrow()
    boardService.deleteBoard(boardId, signinUser)
  }

  @PostMapping("/boards/{boardId}/hit")
  fun hit(@PathVariable boardId: Long, request: HttpServletRequest) {
    val ip = ServletUtils.getIp(request)

    if (signinUser.exists())
      boardService.hitBoard(boardId, ip, signinUser)
    else
      boardService.hitBoard(boardId, ip)
  }

  @PutMapping("/boards/{boardId}/like-or-hate")
  fun likeOrHate(@PathVariable boardId: Long,
                 @RequestBody @Valid req: BoardLIkeReq): BoardLikeRes {
    signinUser.unexistsThrow()
    return boardService.readBoard(boardId, req.likeOrHate, signinUser)
  }
}