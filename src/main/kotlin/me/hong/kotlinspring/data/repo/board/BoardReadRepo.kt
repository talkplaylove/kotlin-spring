package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardReadId
import me.hong.kotlinspring.data.repo.board.custom.BoardReadCustomRepo
import org.springframework.data.jpa.repository.JpaRepository

interface BoardReadRepo : JpaRepository<BoardRead, BoardReadId>, BoardReadCustomRepo