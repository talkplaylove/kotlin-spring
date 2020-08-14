package me.hong.kotlinspring.data.repo.user

import me.hong.kotlinspring.data.entity.user.UserAccess
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.data.repo.user.custom.UserAccessCustomRepo
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccessRepo : JpaRepository<UserAccess, UserAccessId>, UserAccessCustomRepo