package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardLike

interface BoardLikeCustomRepo {
  fun insert(boardLike: BoardLike): BoardLike
}