package me.project3.demo.usecase.user

import me.project3.demo.common.inout.Paging
import me.project3.demo.entity.point.Point
import me.project3.demo.entity.users.Users

data class UserCreateCmd(
    val email: String,
    val password: String
)

data class UserCreateRes(
    val user: Users.Vo,
    val point: Point.Vo
)

data class UserSearchCmd(
    val email: String?,
    val active: Boolean?,
    val minimumPoint: Long?,
    val paging: Paging
)

