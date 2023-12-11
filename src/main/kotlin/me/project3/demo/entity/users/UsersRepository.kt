package me.project3.demo.entity.users

import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<Users, Long> {
    fun findByEmail(email: String): Users?
}