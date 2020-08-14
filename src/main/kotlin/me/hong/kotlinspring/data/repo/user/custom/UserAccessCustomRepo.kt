package me.hong.kotlinspring.data.repo.user.custom

import me.hong.kotlinspring.data.entity.user.UserAccess

interface UserAccessCustomRepo {
  fun insert(userAccess: UserAccess): UserAccess
}