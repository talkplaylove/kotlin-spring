package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.user.*
import me.hong.kotlinspring.web.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Validated
@RestController
class UserController(
    private val userService: UserService,
    private val userSession: UserSession
) {
  @GetMapping("/users/email/duplicate")
  fun duplicateEmail(@RequestParam @Email email: String): UserDuplicateRes {
    return userService.duplicateEmail(email)
  }

  @GetMapping("/users/name/duplicate")
  fun duplicateName(@RequestParam @Size(min = 1) name: String): UserDuplicateRes {
    return userService.duplicateName(name)
  }

  @PostMapping("/users/signin")
  fun signin(@RequestBody req: SigninReq): SigninRes {
    val res = userService.signin(req)
    userSession.set(res.id!!, res.name)
    return res
  }

  @PostMapping("/users/signup")
  @ResponseStatus(HttpStatus.CREATED)
  fun signup(@RequestBody req: SignupReq): SignupRes {
    return userService.signup(req)
  }
}