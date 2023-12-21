package me.project3.demo.service.users.impl

import me.project3.demo.common.inout.AppException
import me.project3.demo.entity.users.Users
import me.project3.demo.entity.users.UsersRepository
import me.project3.demo.service.BaseService
import me.project3.demo.service.users.IUsers
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import javax.transaction.Transactional

@Component
@Transactional
class UsersService(
    private val repository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
): IUsers, BaseService() {
    private fun getById(id: Long): Users {
        return repository.findByIdOrNull(id)
            ?: throw AppException("존재하지 않는 user id 입니다. id: $id")
    }

    override fun create(email: String, password: String): Users.Vo {
        val duplicatedEmailUser = repository.findByEmail(email)
        duplicatedEmailUser?.run {
            throw AppException("이미 존재하는 email 입니다. email: $email")
        }

        val user = Users(email, passwordEncoder.encode(password))

        return repository.save(user).toVo()
    }

    override fun changePassword(id: Long, password: String): Users.Vo {
        val user = getById(id)

        if (user.password == password) {
            throw AppException("기존 비밀번호와 동일합니다.")
        }

        // 패스워드는 절대 평문으로 저장하면 hash 함수로 저장해야 한다.!!! 일단 테스트목적
        with(user) {
            this.password = passwordEncoder.encode(password)
            this.updatedAt = Timestamp.from(Instant.now())
        }

        return repository.save(user).toVo()
    }


}