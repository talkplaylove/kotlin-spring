package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardRead
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardReadCustomRepoImpl : BoardReadCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(read: BoardRead): BoardRead {
    entityManager.persist(read)
    return read
  }
}