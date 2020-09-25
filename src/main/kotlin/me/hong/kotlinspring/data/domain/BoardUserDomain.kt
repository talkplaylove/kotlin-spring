package me.hong.kotlinspring.data.domain

import me.hong.kotlinspring.data.entity.board.Board
import me.hong.kotlinspring.data.entity.board.BoardComment
import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.repo.board.BoardUserRepo
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class BoardUserDomain(
    private val boardUserRepo: BoardUserRepo
) {
  fun getBoardUser(userId: Long?): BoardUser? {
    return boardUserRepo.findById(userId)
  }

  fun getCommentUsers(comments: Page<BoardComment>): Map<Long?, BoardUser> {
    val userIds = comments.stream().map { it.createdBy }.collect(Collectors.toSet())
    return this.getBoardUsers(userIds)
  }

  fun getBoardUsers(boards: Page<Board>): Map<Long?, BoardUser> {
    val userIds = boards.stream().map { it.createdBy }.collect(Collectors.toSet())
    return this.getBoardUsers(userIds)
  }

  fun getBoardUsers(userIds: Set<Long?>): Map<Long?, BoardUser> {
    return boardUserRepo.findAllById(userIds)
        .stream().collect(Collectors.toMap(BoardUser::userId) { it }).toMap()
  }

  fun createBoardUser(boardUser: BoardUser): BoardUser {
    return this.getBoardUser(boardUser.userId)
        ?: boardUserRepo.insert(boardUser)
  }
}