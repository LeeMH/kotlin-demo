package me.project3.demo.usecase.user

import me.project3.demo.conroller.UserSearchOut
import org.springframework.data.domain.Page

interface UserUseCase {
    fun create(cmd: UserCreateCmd): UserCreateRes

    fun search(cmd: UserSearchCmd): Page<UserSearchOut>
}