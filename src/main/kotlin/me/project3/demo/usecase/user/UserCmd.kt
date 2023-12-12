package me.project3.demo.usecase.user

data class UserCreateCmd(
    val email: String,
    val password: String
)