package me.project3.demo.usecase.user.app

import me.project3.demo.config.JwtTokenUtil
import me.project3.demo.conroller.UserLoginOut
import me.project3.demo.conroller.UserSearchOut
import me.project3.demo.service.users.IUserQuery
import me.project3.demo.service.users.impl.UserSearchParam
import me.project3.demo.usecase.user.UserQueryUseCase
import me.project3.demo.usecase.user.UserSearchCmd
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserQueryApp(
    private val userService: IUserQuery,
    private val jwtTokenUtil: JwtTokenUtil
): UserQueryUseCase {
    override fun search(cmd: UserSearchCmd): Page<UserSearchOut> {
        val param = UserSearchParam(
            cmd.email,
            cmd.active,
            cmd.minimumPoint,
            cmd.paging
        )

        val total = userService.count(param)

        val rows = when(total > 0) {
            true -> userService.search(param)
            false -> emptyList()
        }

        // 실제로 updatedAt등의 값을 yyyymmdd등 다른 포맷으로 변경하더라도 service layer는 이상이 없다.
        // 데이터만 서빙할뿐, 데이터 트랜스폼은 그들의 관심사가 아니다..
        val result = rows.map {
            UserSearchOut(
                UserSearchOut.User(
                    id = it.user.id,
                    email = it.user.email,
                    active = it.user.active,
                    createdAt = it.user.createdAt.time
                ),
                UserSearchOut.Point(
                    balance = it.point.balance,
                    beforeBalance = it.point.beforeBalance,
                    updatedAt = it.point.updatedAt.time
                )
            )
        }

        return PageImpl(result, cmd.paging.getPageRequest(), total)
    }

    override fun login(email: String, password: String): UserLoginOut {
        val user = userService.getByEmail(email)

        userService.matchPassword(user.id, password)

        return UserLoginOut(
            user.id,
            jwtTokenUtil.generateToken(user.id, user.email, listOf( SimpleGrantedAuthority("USER"))),
            jwtTokenUtil.generateRefreshToken(user.id, user.email, listOf( SimpleGrantedAuthority("USER"))),
        )

    }
}