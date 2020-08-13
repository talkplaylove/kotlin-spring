package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardLike
import me.hong.kotlinspring.data.entity.board.embedded.BoardLikeId
import me.hong.kotlinspring.data.repo.board.custom.BoardLikeCustomRepo
import org.springframework.data.jpa.repository.JpaRepository

interface BoardLikeRepo : JpaRepository<BoardLike, BoardLikeId>, BoardLikeCustomRepo