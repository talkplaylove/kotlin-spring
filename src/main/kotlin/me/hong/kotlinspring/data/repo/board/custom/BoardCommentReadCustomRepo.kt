package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardCommentRead

interface BoardCommentReadCustomRepo {
  fun insert(read: BoardCommentRead): BoardCommentRead
}