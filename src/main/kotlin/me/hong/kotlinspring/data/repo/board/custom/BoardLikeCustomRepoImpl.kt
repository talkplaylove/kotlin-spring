package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardLike
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardLikeCustomRepoImpl : BoardLikeCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(boardLike: BoardLike): BoardLike {
    entityManager.persist(boardLike)
    return boardLike
  }
}