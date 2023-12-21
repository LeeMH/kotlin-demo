package me.project3.demo.service.users.impl

import me.project3.demo.common.inout.AppException
import me.project3.demo.entity.point.QPoint
import me.project3.demo.entity.users.QUsers
import me.project3.demo.entity.users.Users
import me.project3.demo.entity.users.UsersRepository
import me.project3.demo.service.BaseService
import me.project3.demo.service.users.IUserQuery
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class UserQueryService(
    private val repository: UsersRepository
): IUserQuery, BaseService() {
    override fun findByEmail(email: String): Users.Vo? {
        return repository.findByEmail(email)?.toVo()
    }

    override fun getByEmail(email: String): Users.Vo {
        return repository.findByEmail(email)?.toVo()
            ?: throw AppException("존재하지 않는 user email 입니다. email: $email")
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