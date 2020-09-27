package me.hong.kotlinspring.data.domain.board

import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.repo.board.BoardUserRepo
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class BoardUserDomain(
    private val boardUserRepo: BoardUserRepo
) {
  fun getOne(userId: Long?): BoardUser? {
    return boardUserRepo.findById(userId)
  }

  fun getMap(userIds: Set<Long?>): Map<Long?, BoardUser> {
    return boardUserRepo.findAllById(userIds)
        .stream().collect(Collectors.toMap(BoardUser::userId) { it }).toMap()
  }

  fun create(boardUser: BoardUser): BoardUser {
    return this.getOne(boardUser.userId)
        ?: boardUserRepo.insert(boardUser)
  }
}