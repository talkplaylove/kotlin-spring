package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.model.user.SigninReq
import me.hong.kotlinspring.web.model.user.SigninRes
import me.hong.kotlinspring.web.model.user.SignupReq
import me.hong.kotlinspring.web.model.user.SignupRes
import me.hong.kotlinspring.web.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

  @PostMapping("/user/signin")
  fun signin(@RequestBody req: SigninReq): SigninRes {
    return userService.signin(req)
  }

  @PostMapping("/user/signup")
  @ResponseStatus(HttpStatus.CREATED)
  fun signup(@RequestBody req: SignupReq): SignupRes {
    return userService.signup(req)
  }

}