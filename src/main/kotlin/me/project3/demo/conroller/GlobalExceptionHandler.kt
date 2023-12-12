package me.project3.demo.conroller

import me.project3.demo.common.inout.AppException
import me.project3.demo.common.inout.AppResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [AppException::class])
    fun handleAppException(ex: AppException): AppResponse<String> {
        return AppResponse.error(ex.message!!)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationError(ex: MethodArgumentNotValidException): AppResponse<String> {
        val message = (ex.bindingResult?.fieldErrors?.get(0) as FieldError).defaultMessage
            ?: "잘못된 요청 입니다."

        return AppResponse.error(message)
    }


}