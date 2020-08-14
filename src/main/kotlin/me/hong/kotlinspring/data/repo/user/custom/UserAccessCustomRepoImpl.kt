package me.hong.kotlinspring.data.repo.user.custom

import me.hong.kotlinspring.data.entity.user.UserAccess
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class UserAccessCustomRepoImpl : UserAccessCustomRepo {

  @PersistenceContext
  lateinit var entityManager: EntityManager

  override fun insert(userAccess: UserAccess): UserAccess {
    entityManager.persist(userAccess)
    return userAccess
  }
}