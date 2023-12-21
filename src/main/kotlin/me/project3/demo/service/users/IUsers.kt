package me.project3.demo.service.users

import me.project3.demo.entity.users.Users

interface IUsers {
    fun create(email: String, password: String): Users.Vo

    fun changePassword(id: Long, password: String): Users.Vo
}