package me.hong.kotlinspring.data.repo

import me.hong.kotlinspring.data.entity.Board
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardCustomRepoImpl : BoardCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun remove(board: Board) {
    entityManager.remove(board)
  }
}