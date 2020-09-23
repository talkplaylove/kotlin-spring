package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardUser
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardUserCustomRepoImpl : BoardUserCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(boardUser: BoardUser): BoardUser {
    entityManager.persist(boardUser)
    return boardUser
  }
}