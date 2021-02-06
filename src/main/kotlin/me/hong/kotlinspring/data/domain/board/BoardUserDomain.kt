package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.repo.board.BoardUserRepo
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class BoardUserDomain(
  private val boardUserRepo: BoardUserRepo
) {
  fun getOptional(userId: Long): Optional<BoardUser> {
    return boardUserRepo.findById(userId)
  }

  fun getOneNullable(userId: Long?): BoardUser? {
    return boardUserRepo.findById(userId)
  }

  fun getMap(userIds: Set<Long?>): Map<Long?, BoardUser> {
    return boardUserRepo.findAllById(userIds)
      .stream().collect(Collectors.toMap(BoardUser::userId) { it }).toMap()
  }

  fun create(boardUser: BoardUser): BoardUser {
    return boardUserRepo.insert(boardUser)
  }
}