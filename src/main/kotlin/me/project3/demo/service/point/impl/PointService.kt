package me.project3.demo.service.point.impl

import me.project3.demo.common.inout.AppException
import me.project3.demo.entity.point.Point
import me.project3.demo.entity.point.PointRepository
import me.project3.demo.service.point.IPoint
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import javax.transaction.Transactional

@Service
@Transactional
class PointService(
    private val repository: PointRepository
): IPoint {
    override fun findByUserId(userId: Long): Point.Vo? {
        return repository.findByIdOrNull(userId)?.toVo()
    }

    override fun getByUserId(userId: Long): Point.Vo {
        return repository.findByIdOrNull(userId)?.toVo()
            ?: throw AppException("존재하지 않는 user id 입니다. id: $userId")
    }

    override fun create(userId: Long): Point.Vo {
        val duplicatedUser = findByUserId(userId)
        duplicatedUser?.run {
            throw AppException("이미 존재하는 user id 입니다. id: $userId")
        }

        val point = Point(userId)
        return repository.save(point).toVo()
    }

    override fun plus(userId: Long, amount: Long, type: Point.Type): Point.Vo {
        if (amount <= 0) {
            throw AppException("포인트는 0보다 커야합니다. amount: $amount")
        }

        val point = repository.getById(userId)

        with(point) {
            this.beforeBalance = this.balance
            this.balance += amount
            this.updatedAt = Timestamp.from(Instant.now())
        }

        return repository.save(point).toVo()
    }

    override fun minus(userId: Long, amount: Long, type: Point.Type): Point.Vo {
        if (amount <= 0) {
            throw AppException("포인트는 0보다 커야합니다. amount: $amount")
        }

        val point = repository.getById(userId)
        if (point.balance < amount) {
            throw AppException("잔액이 부족합니다. balance: ${point.balance}, amount: $amount")
        }

        with(point) {
            this.beforeBalance = this.balance
            this.balance -= amount
            this.updatedAt = Timestamp.from(Instant.now())
        }

        return repository.save(point).toVo()
    }

}