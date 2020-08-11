package me.hong.kotlinspring.data.repo

import me.hong.kotlinspring.data.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepo : JpaRepository<Board, Long>, BoardCustomRepo {
}