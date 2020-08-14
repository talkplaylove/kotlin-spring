package me.hong.kotlinspring.web.service

import me.hong.kotlinspring.data.entity.user.User
import me.hong.kotlinspring.data.entity.user.UserAccess
import me.hong.kotlinspring.data.entity.user.embedded.UserAccessId
import me.hong.kotlinspring.data.repo.user.UserAccessRepo
import me.hong.kotlinspring.data.repo.user.UserRepo
import me.hong.kotlinspring.web.advice.CustomException
import me.hong.kotlinspring.web.advice.CustomMessage
import me.hong.kotlinspring.web.model.user.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Collectors

@Service
class UserService(
    private val userRepo: UserRepo,
    private val userAccessRepo: UserAccessRepo,
    private val passwordEncoder: PasswordEncoder
) {
  @Transactional
  fun signin(req: SigninReq): SigninRes {
    val user = userRepo.findByEmail(req.email)
        ?: throw CustomException(CustomMessage.USER_NOT_FOUND)

    if (!passwordEncoder.matches(req.password, user.password))
      throw CustomException(CustomMessage.INCORRECT_PASSWORD)

    val userAccessId = UserAccessId(user.id!!, LocalDate.now())
    userAccessRepo.findById(userAccessId).ifPresentOrElse({
      it.hit++
    }, {
      userAccessRepo.insert(UserAccess(userAccessId))
    })

    return SigninRes.of(user)
  }

  fun signup(req: SignupReq): SignupRes {
    val encodedPassword = passwordEncoder.encode(req.password)
    val user = userRepo.save(req.toEntity(encodedPassword))

    return SignupRes.of(user)
  }

  fun duplicateEmail(email: String): UserDuplicateRes {
    val duplicated = userRepo.existsByEmail(email)

    return UserDuplicateRes.of(duplicated)
  }

  fun duplicateName(name: String): UserDuplicateRes {
    val duplicated = userRepo.existsByName(name)

    return UserDuplicateRes.of(duplicated)
  }

  fun getUser(id: Long?): User? {
    return userRepo.findById(id)
  }

  fun getUsers(ids: Set<Long?>): Map<Long?, User> {
    val users = userRepo.findAllById(ids)
    return users.stream().collect(Collectors.toMap(User::id) { it }).toMap()
  }
}