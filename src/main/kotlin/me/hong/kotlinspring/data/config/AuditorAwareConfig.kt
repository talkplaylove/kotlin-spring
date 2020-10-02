package me.hong.kotlinspring.data.config

import me.hong.kotlinspring.web.advice.SigninUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class AuditorAwareConfig(
    private val signinUser: SigninUser
) {
  @Bean
  fun auditorAware(): AuditorAware<Long> {
    return AuditorAware { Optional.of(signinUser.id) }
  }
}