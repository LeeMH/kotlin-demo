package me.project3.demo.usecase.user.app

import me.project3.demo.conroller.UserCreateOut
import me.project3.demo.service.point.IPoint
import me.project3.demo.service.point.IPointHistory
import me.project3.demo.service.users.IUsers
import me.project3.demo.usecase.user.UserCreateCmd
import me.project3.demo.usecase.user.UserUseCase
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserApp(
    private val userService: IUsers,
    private val pointService: IPoint,
    private val pointHistoryService: IPointHistory
): UserUseCase {
    override fun create(cmd: UserCreateCmd): UserCreateOut {
        val user = userService.create(cmd.email, cmd.password)
        val point = pointService.create(user.id)
        val history = pointHistoryService.create(point)

        return UserCreateOut(
            user.id,
            user.email,
            user.active,
            point.balance,
        )
    }
}