package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepo : JpaRepository<Board, Long>, BoardCustomRepo