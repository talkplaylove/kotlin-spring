package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.Board
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardCustomRepoImpl : BoardCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun remove(board: Board) {
    entityManager.remove(board)
  }
}