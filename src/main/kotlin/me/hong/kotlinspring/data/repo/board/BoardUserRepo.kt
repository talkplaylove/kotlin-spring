package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardUser
import org.springframework.data.jpa.repository.JpaRepository

interface BoardUserRepo : JpaRepository<BoardUser, Long>