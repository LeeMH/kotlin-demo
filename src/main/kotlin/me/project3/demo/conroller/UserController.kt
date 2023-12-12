package me.project3.demo.conroller

import me.project3.demo.common.inout.AppResponse
import me.project3.demo.service.point.IPoint
import me.project3.demo.service.point.IPointHistory
import me.project3.demo.service.users.IUsers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: IUsers,
    private val pointService: IPoint,
    private val pointHistoryService: IPointHistory,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/create")
    @Transactional
    fun create(@Valid @RequestBody dto: UserCreateIn): AppResponse<UserCreateOut> {
        log.info("REQ :: $dto")
        val user = userService.create(dto.email!!, dto.password!!)
        val point = pointService.create(user.id)
        val history = pointHistoryService.create(point)

        val result = UserCreateOut(
            user.id,
            user.email,
            user.active,
            point.balance,
        )

        log.info("RES :: $result")

        return AppResponse.ok(result)
    }

}