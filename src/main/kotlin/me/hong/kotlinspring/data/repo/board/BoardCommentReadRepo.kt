package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import me.hong.kotlinspring.data.entity.board.embedded.BoardCommentReadId
import me.hong.kotlinspring.data.repo.board.custom.BoardCommentReadCustomRepo
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentReadRepo : JpaRepository<BoardCommentRead, BoardCommentReadId>, BoardCommentReadCustomRepo