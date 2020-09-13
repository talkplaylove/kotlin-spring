package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.user.SigninReq
import me.hong.kotlinspring.web.model.user.SigninRes
import me.hong.kotlinspring.web.model.user.SignupReq
import me.hong.kotlinspring.web.model.user.SignupRes
import me.hong.kotlinspring.web.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@RestController
@Validated
class UserController(
    private val userService: UserService,
    private val userSession: UserSession
) {
  @GetMapping("/users/emails/{email}/duplicate")
  fun duplicateEmail(@PathVariable @Email email: String) {
    userService.duplicateEmail(email)
  }

  @GetMapping("/users/names/{name}/duplicate")
  fun duplicateName(@PathVariable @Size(min = 1) name: String) {
    userService.duplicateName(name)
  }

  @PostMapping("/users/signin")
  fun signin(@RequestBody req: SigninReq): SigninRes {
    val res = userService.signin(req)
    userSession.set(res.id!!, res.name)
    return res
  }

  @PostMapping("/users/signup")
  fun signup(@RequestBody req: SignupReq): SignupRes {
    return userService.signup(req)
  }
}