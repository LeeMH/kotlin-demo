package me.project3.demo.service.users

import me.project3.demo.entity.users.Users

interface IUsers {
    fun findByEmail(email: String): Users.Vo?

    fun getByEmail(email: String): Users.Vo

    fun create(email: String, password: String): Users.Vo

    fun changePassword(id: Long, password: String): Users.Vo
}