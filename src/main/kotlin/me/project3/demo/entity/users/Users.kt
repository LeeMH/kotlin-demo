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
    @Column(name = "email", columnDefinition = "varchar(16)", nullable = false)
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
}