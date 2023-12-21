package me.project3.demo.usecase.user

interface UserUseCase {
    fun create(cmd: UserCreateCmd): UserCreateRes
}