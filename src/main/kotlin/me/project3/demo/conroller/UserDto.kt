package me.project3.demo.conroller

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