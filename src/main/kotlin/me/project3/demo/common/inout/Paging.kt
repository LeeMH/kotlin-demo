package me.project3.demo.common.inout

import org.springframework.data.domain.PageRequest

data class Paging(
    val page: Int = 0,

    var size: Int = 10
) {
    fun getOffset(): Long {
        return (page * size).toLong()
    }

    fun getSize(): Long = size.toLong()

    fun getPageRequest(): PageRequest {
        return PageRequest.of(page, size)
    }
}