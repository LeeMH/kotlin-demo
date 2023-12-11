package me.project3.demo.entity.point

import org.springframework.data.jpa.repository.JpaRepository

interface PointRepository : JpaRepository<Point, Long> {
    // 이건 AI값이 PK가 아니라, user_id가 PK 이다.
    //fun findByUserId(userId: Long): Point?
}