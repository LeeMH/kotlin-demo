package me.project3.demo.conroller

import io.swagger.annotations.ApiModelProperty
import me.project3.demo.common.inout.Paging
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserCreateIn(
    @field:NotBlank(message = "이메일은 필수 입니다.")
    @field:Email(message = "이메일 형식이 아닙니다.")
    @ApiModelProperty(value = "이메일", example = "hello@world.com", required = true)
    val email: String?,

    @field:Length(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
    @ApiModelProperty(value = "패스워드", example = "test1234", required = true)
    val password: String?
)

data class UserCreateOut(
    @ApiModelProperty(value = "user #id", example = "1234", required = true)
    val id: Long,

    @ApiModelProperty(value = "이메일", example = "hello@world.com", required = true)
    val email: String,

    @ApiModelProperty(value = "활성화", example = "true", required = true)
    val active: Boolean,

    @ApiModelProperty(value = "포인트", example = "1234", required = true)
    val point: Long
)

data class UserSearchIn(
    @ApiModelProperty(value = "이메일", example = "hello@world.com")
    val email: String?, // 검색 이메일

    @ApiModelProperty(value = "활성화 여부", example = "true")
    val active: Boolean?, // 활성화 여부

    @ApiModelProperty(value = "최소 보유 포인트", example = "1000")
    val minimumPoint: Long?, // 최소 보유 포인트

    @ApiModelProperty(value = "페이징")
    val paging: Paging = Paging() // 페이징
)


data class UserSearchOut(
    @ApiModelProperty(value = "유저", required = true)
    val user: User,

    @ApiModelProperty(value = "포인트", required = true)
    val point: Point
) {
    data class User(
        @ApiModelProperty(value = "user #id", required = true)
        val id: Long,

        @ApiModelProperty(value = "이메일", required = true)
        val email: String,

        @ApiModelProperty(value = "활성화", required = true)
        val active: Boolean,

        @ApiModelProperty(value = "가입일시", required = true)
        val createdAt: Long,
    )

    data class Point(
        @ApiModelProperty(value = "보유 포인트", required = true)
        val balance: Long,

        @ApiModelProperty(value = "직전 포인트", required = true)
        val beforeBalance: Long,

        @ApiModelProperty(value = "최종갱신일시", required = true)
        val updatedAt: Long
    )
}
