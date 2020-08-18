package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardRead

interface BoardReadCustomRepo {
  fun insert(boardRead: BoardRead): BoardRead
}