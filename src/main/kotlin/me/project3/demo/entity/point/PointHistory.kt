package me.project3.demo.entity.point

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "`point_history`")
class PointHistory(
    // 회원 id
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    // 포인트 거래 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(16)", nullable = false)
    val type: Point.Type = Point.Type.CREATED,

    // 포인트 금액
    @Column(name = "amount", nullable = false)
    var amount: Long = 0,

    // 변경전 포인트
    @Column(name = "bef_bal", nullable = false)
    var beforeBalance: Long = 0,

    // 변경후 포인트
    @Column(name = "bal", nullable = false)
    var balance: Long = 0,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    val createdAt: Timestamp = Timestamp.from(Instant.now())

    @Version
    @Column(name = "version", nullable = false)
    val version: Int = 0

    class Vo(
        val id: Long,
        val userId: Long,
        val type: Point.Type,
        val amount: Long,
        val beforeBalance: Long,
        val balance: Long,
        val createdAt: Timestamp,
    )

    fun toVo(): Vo {
        return Vo(id!!, userId, type, amount, beforeBalance, balance, createdAt)
    }
}