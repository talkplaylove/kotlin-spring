package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardHit
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardHitCustomRepoImpl : BoardHitCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(hit: BoardHit): BoardHit {
    entityManager.persist(hit)
    return hit
  }
}