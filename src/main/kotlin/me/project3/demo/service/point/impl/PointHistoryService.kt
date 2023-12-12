package me.project3.demo.service.point.impl

import me.project3.demo.entity.point.Point
import me.project3.demo.entity.point.PointHistory
import me.project3.demo.entity.point.PointHistoryRepository
import me.project3.demo.service.point.IPointHistory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class PointHistoryService(
    private val repository: PointHistoryRepository
): IPointHistory {
    override fun create(point: Point.Vo): PointHistory.Vo {
        val history = PointHistory(
            userId = point.userId
        )

        return repository.save(history).toVo()
    }

    override fun plus(point: Point.Vo, amount: Long, type: Point.Type): PointHistory.Vo {
        val history = PointHistory(
            userId = point.userId,
            amount = amount,
            type = type,
            beforeBalance = point.balance,
            balance = point.balance + amount
        )

        return repository.save(history).toVo()
    }

    override fun minus(point: Point.Vo, amount: Long, type: Point.Type): PointHistory.Vo {
        val history = PointHistory(
            userId = point.userId,
            amount = amount,
            type = type,
            beforeBalance = point.balance,
            balance = point.balance - amount
        )

        return repository.save(history).toVo()

    }
}