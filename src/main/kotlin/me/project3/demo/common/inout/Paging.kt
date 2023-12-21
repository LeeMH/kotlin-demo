package me.project3.demo.common.inout

import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.PageRequest

data class Paging(
    @ApiModelProperty(value = "조회 페이지", example = "0")
    val page: Int = 0,

    @ApiModelProperty(value = "페이지당 목록수", example = "10")
    val size: Int = 10
) {
    fun getOffset(): Long {
        return (page * size).toLong()
    }

    fun getSize(): Long = size.toLong()

    fun getPageRequest(): PageRequest {
        return PageRequest.of(page, size)
    }
}