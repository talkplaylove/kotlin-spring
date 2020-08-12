package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.Board

interface BoardCustomRepo {
  fun remove(board: Board)
}