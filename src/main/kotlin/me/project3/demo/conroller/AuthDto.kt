package me.project3.demo.conroller

import io.swagger.annotations.ApiModelProperty
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

data class UserLoginIn(
    @field:NotBlank(message = "이메일은 필수 입니다.")
    @ApiModelProperty(value = "이메일", example = "hello@world.com", required = true)
    val email: String?,

    @field:NotBlank(message = "패스워드는 필수 입니다.")
    @ApiModelProperty(value = "패스워드", example = "test1234", required = true)
    val password: String?
)

data class UserLoginOut(
    @ApiModelProperty(value = "이메일", required = true)
    val id: Long,

    @ApiModelProperty(value = "토큰", required = true)
    val token: String,

    @ApiModelProperty(value = "리프레쉬 토큰", required = true)
    val refreshToken: String,
)
