package me.project3.demo.service.users

import me.project3.demo.entity.users.Users
import me.project3.demo.service.users.impl.UserSearchParam
import me.project3.demo.service.users.impl.UserSearchResult

interface IUserQuery {
    fun findByEmail(email: String): Users.Vo?

    fun getByEmail(email: String): Users.Vo

    fun count(param: UserSearchParam): Long

    fun search(param: UserSearchParam): List<UserSearchResult>
}