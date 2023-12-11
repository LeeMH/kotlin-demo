package me.project3.demo.entity.users

import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "`users`",
    indexes = [
        Index(name = "IDX_UNIQUE1", columnList = "`email`", unique = true),
    ]
)
class Users(
    @Column(name = "email", columnDefinition = "varchar(64)", nullable = false)
    val email: String,

    @Column(name = "password", columnDefinition = "varchar(255)", nullable = false)
    var password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null

    @Column(name = "active", columnDefinition="bit(1)", nullable = false)
    var active: Boolean = true // false 이면 로그인이 불가능하다.

    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    val createdAt: Timestamp = Timestamp.from(Instant.now())

    @Column(name = "updated_at", columnDefinition = "timestamp", nullable = false)
    var updatedAt: Timestamp = Timestamp.from(Instant.now())

    @Version
    @Column(name = "version", nullable = false)
    val version: Int = 0

    // 서비스 외부로 노출되는 Value Object (read only)
    class Vo(
        val id: Long,
        val email: String,
        val active: Boolean,
        val createdAt: Timestamp,
        val updatedAt: Timestamp,
    )

    // id 값은 AI 값이기 때문에 최초 null 이지만, DB에 저장되면 자동 셋툉된다. 따라서 NotNull을 확정해 준다. !!
    fun toVo(): Vo {
        return Vo(id!!, email, active, createdAt, updatedAt)
    }
}