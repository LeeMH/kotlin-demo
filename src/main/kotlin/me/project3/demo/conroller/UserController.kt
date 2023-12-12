package me.project3.demo.conroller

import me.project3.demo.common.inout.AppResponse
import me.project3.demo.usecase.user.UserCreateCmd
import me.project3.demo.usecase.user.UserSearchCmd
import me.project3.demo.usecase.user.UserUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userApp: UserUseCase
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody dto: UserCreateIn): AppResponse<UserCreateOut> {
        log.info("REQ :: $dto")

        val cmd = UserCreateCmd(
            dto.email!!,
            dto.password!!,
        )

        val res = userApp.create(cmd)

        // 아웃풋 포맷으로 컨트롤러 단에서 맞추기
        val result = UserCreateOut(
            res.user.id,
            res.user.email,
            res.user.active,
            res.point.balance,
        )

        log.info("RES :: $result")

        return AppResponse.ok(result)
    }

    @PostMapping("/search")
    fun search(@Valid @RequestBody dto: UserSearchIn): AppResponse<Page<UserSearchOut>> {
        log.info("REQ :: $dto")

        val cmd = UserSearchCmd(
            dto.email,
            dto.active,
            dto.minimumPoint,
            dto.paging,
        )

        val result = userApp.search(cmd)

        log.info("RES :: $result")

        return AppResponse.ok(result)
    }

}