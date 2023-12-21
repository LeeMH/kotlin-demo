package me.project3.demo.conroller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import me.project3.demo.common.inout.AppResponse
import me.project3.demo.usecase.user.UserCreateCmd
import me.project3.demo.usecase.user.UserQueryUseCase
import me.project3.demo.usecase.user.UserUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["auth"])
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userApp: UserUseCase,
    private val userQueryApp: UserQueryUseCase
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ApiOperation(value = "User 생성")
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


    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: UserLoginIn): AppResponse<UserLoginOut> {
        log.info("REQ :: $dto")

        val res = userQueryApp.login(dto.email!!, dto.password!!)

        log.info("RES :: $res")

        return AppResponse.ok(res)
    }


}