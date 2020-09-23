package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardUser
import me.hong.kotlinspring.data.repo.board.custom.BoardUserCustomRepo
import org.springframework.data.jpa.repository.JpaRepository

interface BoardUserRepo : JpaRepository<BoardUser, Long>, BoardUserCustomRepo {

  fun findById(userId: Long?): BoardUser?
}