package me.project3.demo.service.point

import me.project3.demo.entity.point.Point
import me.project3.demo.entity.point.PointHistory

interface IPointHistory {
    fun create(point: Point.Vo): PointHistory.Vo

    fun plus(point: Point.Vo, amount: Long, type: Point.Type): PointHistory.Vo

    fun minus(point: Point.Vo, amount: Long, type: Point.Type): PointHistory.Vo
}