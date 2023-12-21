package me.project3.demo.conroller

import io.swagger.annotations.ApiModelProperty
import me.project3.demo.common.inout.Paging


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
