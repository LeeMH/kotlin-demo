package me.project3.demo.entity.point

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "`point`")
class Point(
    @Id
    @Column(name = "user_id")
    val userId: Long
) {
    @Column(name = "bef_bal", nullable = false)
    var beforeBalance: Long = 0

    @Column(name = "bal", nullable = false)
    var balance: Long = 0

    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    val createdAt: Timestamp = Timestamp.from(Instant.now())

    @Column(name = "updated_at", columnDefinition = "timestamp", nullable = false)
    var updatedAt: Timestamp = Timestamp.from(Instant.now())

    @Version
    @Column(name = "version", nullable = false)
    val version: Int = 0

    enum class Type {
        CREATED,  // 최초 생성
        REWARDED,  // 리워드 부여
        USED // 사용
    }

    data class Vo(
        val userId: Long,
        val beforeBalance: Long,
        val balance: Long,
        val createdAt: Timestamp,
        val updatedAt: Timestamp
    )

    fun toVo(): Vo {
        return Vo(userId, beforeBalance, balance, createdAt, updatedAt)
    }
}