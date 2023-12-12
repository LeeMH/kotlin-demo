package me.project3.demo.service.users.impl

import me.project3.demo.common.inout.AppException
import me.project3.demo.entity.point.QPoint
import me.project3.demo.entity.users.QUsers
import me.project3.demo.entity.users.Users
import me.project3.demo.entity.users.UsersRepository
import me.project3.demo.service.BaseService
import me.project3.demo.service.users.IUsers
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import javax.transaction.Transactional

@Component
@Transactional
class UsersService(
    // 난 내 서비스만 이용하기 때문에, 굳이 userRepository라 명명할 필요 없다.
    private val repository: UsersRepository
): IUsers, BaseService() {
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

    override fun count(param: UserSearchParam): Long {
        val qUsers = QUsers.users
        val qPoint = QPoint.point

        val where = where()

        // 스코프 함수 run, let, apply, also드 있는데, this등 헤깔리면 그냥 사용해도 무방
        param.email?.run { where.and(qUsers.email.contains(this)) }
        param.active?.run { where.and(qUsers.active.eq(param.active)) }
        param.minimumPoint?.run { where.and(qPoint.balance.goe(this)) }

        return query().select(qUsers.count())
            .from(qUsers)
            .leftJoin(qPoint).on(qUsers.id.eq(qPoint.userId))
            .where(where)
            .fetchOne() ?: 0
    }

    override fun search(param: UserSearchParam): List<UserSearchResult> {
        val qUsers = QUsers.users
        val qPoint = QPoint.point

        val where = where()

        // 스코프 함수 run, let, apply, also드 있는데, this등 헤깔리면 그냥 사용해도 무방
        param.email?.run { where.and(qUsers.email.contains(this)) }
        param.active?.run { where.and(qUsers.active.eq(param.active)) }
        param.minimumPoint?.run { where.and(qPoint.balance.goe(this)) }

        val result = query().select(qUsers, qPoint)
            .from(qUsers)
            .leftJoin(qPoint).on(qUsers.id.eq(qPoint.userId))
            .where(where)
            .orderBy(qUsers.email.asc())
            .offset(param.paging.getOffset())
            .limit(param.paging.getSize())
            .fetch()

        // java의 경우, list.stream.map(...).collect(Collectors.toList()) 이런식으로 사용
        // list를 map하면 당연히 list이고, 그걸 set으로 하겠다 할때만, toSet하면됨
        return result.map {
            UserSearchResult(
                it[qUsers]!!.toVo(),
                it[qPoint]!!.toVo()
            )
        }
    }
}