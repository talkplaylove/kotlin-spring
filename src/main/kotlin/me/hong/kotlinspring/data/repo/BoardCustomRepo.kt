package me.hong.kotlinspring.data.repo

import me.hong.kotlinspring.data.entity.Board

interface BoardCustomRepo {
  fun remove(board: Board)
}