package me.hong.kotlinspring.data.repo.board.custom

import me.hong.kotlinspring.data.entity.board.BoardCommentRead
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class BoardCommentReadCustomRepoImpl : BoardCommentReadCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(read: BoardCommentRead): BoardCommentRead {
    entityManager.persist(read)
    return read
  }
}