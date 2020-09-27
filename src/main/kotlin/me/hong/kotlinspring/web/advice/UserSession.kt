package me.hong.kotlinspring.web.advice

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
class UserSession {
  var id: Long = 0L
  var name: String = ""

  fun set(id: Long, name: String) {
    this.id = id
    this.name = name
  }

  fun exists(): Boolean {
    return this.id != 0L
  }

  fun unexists(): Boolean {
    return !exists()
  }

  fun unexistsThrow() {
    if (unexists())
      throw CustomException(CustomMessage.UNAUTHORIZED)
  }

  fun matches(userId: Long?): Boolean {
    return this.id == userId
  }

  fun unmatches(userId: Long?): Boolean {
    return !matches(userId)
  }

  fun unmatchesThrow(userId: Long?) {
    if (unmatches(userId))
      throw CustomException(CustomMessage.FORBIDDEN)
  }
}