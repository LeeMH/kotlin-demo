package me.project3.demo.service.users.impl

import me.project3.demo.common.inout.Paging
import me.project3.demo.entity.point.Point
import me.project3.demo.entity.users.Users

data class UserSearchParam(
    val id: Long? = null,
    val email: String? = null,
    val active: Boolean? = null,
    val minimumPoint: Long? = null,
    val paging: Paging = Paging()
)

data class UserSearchResult(
    val user: Users.Vo,
    val point: Point.Vo
)