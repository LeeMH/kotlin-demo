package me.project3.demo.service.point

import me.project3.demo.entity.point.Point

interface IPoint {
    fun findByUserId(userId: Long): Point.Vo?

    fun getByUserId(userId: Long): Point.Vo

    fun create(userId: Long): Point.Vo

    fun plus(userId: Long, amount: Long, type: Point.Type): Point.Vo

    fun minus(userId: Long, amount: Long, type: Point.Type): Point.Vo
}