package me.project3.demo.conroller

import me.project3.demo.common.inout.Paging
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserCreateIn(
    @field:NotBlank(message = "이메일은 필수 입니다.")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String?,

    @field:Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
    val password: String?
)

data class UserCreateOut(
    val id: Long,

    val email: String,

    val active: Boolean,

    val point: Long
)

data class UserSearchIn(
    val email: String?, // 검색 이메일

    val active: Boolean?, // 활성화 여부

    val minimumPoint: Long?, // 최소 보유 포인트

    val paging: Paging = Paging() // 페이징
)

// 구조회된 json 만드는 것도 아주 쉽다!!!
data class UserSearchOut(
    val user: User,

    val point: Point
) {
    data class User(
        val id: Long,

        val email: String,

        val active: Boolean,

        val createdAt: Long,
    )

    data class Point(
        val balance: Long,

        val beforeBalance: Long,

        val updatedAt: Long
    )
}
