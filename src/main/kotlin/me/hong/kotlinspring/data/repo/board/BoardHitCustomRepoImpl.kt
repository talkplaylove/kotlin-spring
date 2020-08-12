package me.hong.kotlinspring.data.repo.board

import me.hong.kotlinspring.data.entity.board.BoardHit
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardHitCustomRepoImpl : BoardHitCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(boardHit: BoardHit): BoardHit {
    entityManager.persist(boardHit)
    return boardHit
  }
}