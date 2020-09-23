package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardUser

interface BoardUserCustomRepo {

  fun insert(boardUser: BoardUser): BoardUser
}