package me.hong.kotlinspring.web.controller.user

import me.hong.kotlinspring.web.advice.UserSession
import me.hong.kotlinspring.web.model.user.*
import me.hong.kotlinspring.web.service.user.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
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
  fun signin(@RequestBody @Valid req: SigninReq): SigninRes {
    val res = userService.signin(req)
    userSession.set(res.id!!, res.name)
    return res
  }

  @PostMapping("/users/signup")
  fun signup(@RequestBody @Valid req: SignupReq): SignupRes {
    return userService.signup(req)
  }

  @PatchMapping("/users/{userId}/email")
  fun updateEmail(@PathVariable userId: Long, @RequestBody @Valid req: UserEmailPutReq) {
    userSession.unmatchesThrow(userId)
    userService.updateEmail(req, userSession)
  }

  @PatchMapping("/users/{userId}/name")
  fun updateName(@PathVariable userId: Long, @RequestBody @Valid req: UserNamePutReq) {
    userSession.unmatchesThrow(userId)
    userService.updateName(req, userSession)
    userSession.set(req.name)
  }

  @PatchMapping("/users/{userId}/password")
  fun updatePassword(@PathVariable userId: Long, @RequestBody @Valid req: UserPasswordPutReq) {
    userSession.unmatchesThrow(userId)
    userService.updatePassword(req, userSession)
  }
}