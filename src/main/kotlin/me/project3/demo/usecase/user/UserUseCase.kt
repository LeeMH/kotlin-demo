package me.project3.demo.usecase.user

import me.project3.demo.conroller.UserCreateOut

interface UserUseCase {
    fun create(cmd: UserCreateCmd): UserCreateOut
}