package me.project3.demo.usecase.user

import me.project3.demo.conroller.UserLoginOut
import me.project3.demo.conroller.UserSearchOut
import org.springframework.data.domain.Page

interface UserQueryUseCase {
    fun search(cmd: UserSearchCmd): Page<UserSearchOut>

    fun login(email: String, password: String): UserLoginOut
}