package me.project3.demo.usecase.user.app

import me.project3.demo.conroller.UserCreateOut
import me.project3.demo.conroller.UserSearchOut
import me.project3.demo.service.point.IPoint
import me.project3.demo.service.point.IPointHistory
import me.project3.demo.service.users.IUsers
import me.project3.demo.service.users.impl.UserSearchParam
import me.project3.demo.usecase.user.UserCreateCmd
import me.project3.demo.usecase.user.UserCreateRes
import me.project3.demo.usecase.user.UserSearchCmd
import me.project3.demo.usecase.user.UserUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserApp(
    private val userService: IUsers,
    private val pointService: IPoint,
    private val pointHistoryService: IPointHistory
): UserUseCase {
    override fun create(cmd: UserCreateCmd): UserCreateRes {
        val user = userService.create(cmd.email, cmd.password)
        val point = pointService.create(user.id)
        val history = pointHistoryService.create(point)

        return UserCreateRes(
            user, point
        )
    }

    override fun search(cmd: UserSearchCmd): Page<UserSearchOut> {
        val param = UserSearchParam(
            cmd.email,
            cmd.active,
            cmd.minimumPoint,
            cmd.paging
        )

        val total = userService.count(param)
        //val rows = if (total > 0) userService.search(param) else emptyList()
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
}