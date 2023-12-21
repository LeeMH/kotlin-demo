package me.project3.demo.conroller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import me.project3.demo.common.inout.AppResponse
import me.project3.demo.usecase.user.UserCreateCmd
import me.project3.demo.usecase.user.UserQueryUseCase
import me.project3.demo.usecase.user.UserSearchCmd
import me.project3.demo.usecase.user.UserUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["User"])
@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('ROLE_USER')")
class UserController(
    private val userQueryApp: UserQueryUseCase
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ApiOperation(value = "User 검색")
    @GetMapping("/{id}/get")
    fun search(@PathVariable id: Long): AppResponse<UserSearchOut> {
        log.info("REQ :: $id")

        val result = userQueryApp.getById(id)

        log.info("RES :: $result")

        return AppResponse.ok(result)
    }

    @ApiOperation(value = "User 검색")
    @PostMapping("/search")
    fun search(@Valid @RequestBody dto: UserSearchIn): AppResponse<Page<UserSearchOut>> {
        log.info("REQ :: $dto")

        val cmd = UserSearchCmd(
            dto.email,
            dto.active,
            dto.minimumPoint,
            dto.paging,
        )

        val result = userQueryApp.search(cmd)

        log.info("RES :: $result")

        return AppResponse.ok(result)
    }

}