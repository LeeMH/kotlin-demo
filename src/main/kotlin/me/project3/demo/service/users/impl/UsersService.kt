package me.project3.demo.service.users.impl

import me.project3.demo.common.exception.AppException
import me.project3.demo.entity.users.Users
import me.project3.demo.entity.users.UsersRepository
import me.project3.demo.service.users.IUsers
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import javax.transaction.Transactional

@Service
@Transactional
class UsersService(
    // 난 내 서비스만 이용하기 때문에, 굳이 userRepository라 명명할 필요 없다.
    private val repository: UsersRepository
): IUsers{
    private fun getById(id: Long): Users {
        return repository.findByIdOrNull(id)
            ?: throw AppException("존재하지 않는 user id 입니다. id: $id")
    }

    override fun findByEmail(email: String): Users.Vo? {
        return repository.findByEmail(email)?.toVo()
    }

    override fun getByEmail(email: String): Users.Vo {
        return repository.findByEmail(email)?.toVo()
            ?: throw AppException("존재하지 않는 user email 입니다. email: $email")
    }

    override fun create(email: String, password: String): Users.Vo {
        val duplicatedEmailUser = repository.findByEmail(email)
        duplicatedEmailUser?.run {
            throw AppException("이미 존재하는 email 입니다. email: $email")
        }

        var user = Users(email, password)

        return repository.save(user).toVo()
    }

    override fun changePassword(id: Long, password: String): Users.Vo {
        val user = getById(id)

        if (user.password == password) {
            throw AppException("기존 비밀번호와 동일합니다.")
        }

        // 패스워드는 절대 평문으로 저장하면 hash 함수로 저장해야 한다.!!! 일단 테스트목적
        with(user) {
            this.password = password
            this.updatedAt = Timestamp.from(Instant.now())
        }

        return repository.save(user).toVo()
    }
}