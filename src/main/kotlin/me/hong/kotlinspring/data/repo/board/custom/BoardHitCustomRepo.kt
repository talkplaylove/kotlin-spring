package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardHit

interface BoardHitCustomRepo {
  fun insert(hit: BoardHit): BoardHit
}