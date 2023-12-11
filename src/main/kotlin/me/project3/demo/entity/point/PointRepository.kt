package me.project3.demo.entity.point

import org.springframework.data.jpa.repository.JpaRepository

interface PointRepository : JpaRepository<Point, Long> {
}