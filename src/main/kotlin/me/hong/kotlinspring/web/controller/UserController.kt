package me.hong.kotlinspring.web.controller

import me.hong.kotlinspring.web.model.SigninReq
import me.hong.kotlinspring.web.model.SigninRes
import me.hong.kotlinspring.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

  @Autowired
  lateinit var userService: UserService

  @PostMapping("/user/signin")
  fun signin(@RequestBody req: SigninReq): SigninRes {
    return userService.signin(req)
  }

}