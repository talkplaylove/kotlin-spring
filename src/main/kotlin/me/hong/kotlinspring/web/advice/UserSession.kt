package me.hong.kotlinspring.web.advice

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
class UserSession {
  var id: Long = 0L
  var name: String = "게스트"

  fun exists(): Boolean {
    return this.id == 0L
  }

  fun matches(userId: Long?): Boolean {
    return this.id == userId
  }
}