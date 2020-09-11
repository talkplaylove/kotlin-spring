package me.hong.kotlinspring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class KotlinSpringApplication

fun main(args: Array<String>) {
  val application = SpringApplication(KotlinSpringApplication::class.java)
  application.addListeners(ApplicationPidFileWriter())
  application.run(*args)
}
