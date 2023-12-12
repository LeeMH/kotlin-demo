package me.project3.demo.service.users.impl

import me.project3.demo.common.inout.Paging
import me.project3.demo.entity.point.Point
import me.project3.demo.entity.users.Users

data class UserSearchParam(
    val email: String?,
    val active: Boolean?,
    val minimumPoint: Long?,
    val paging: Paging
)

data class UserSearchResult(
    val user: Users.Vo,
    val point: Point.Vo
)