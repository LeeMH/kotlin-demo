package me.project3.demo.common.inout

import com.fasterxml.jackson.annotation.JsonProperty

class AppResponse<T> {
    @JsonProperty("message")
    private var message: String

    @JsonProperty("error")
    private var error: String

    @JsonProperty("success")
    private var success: Boolean = false

    @JsonProperty("notify")
    private var notify: Boolean = false

    @JsonProperty("data")
    private var t: T? = null

    constructor(message: String, error: String, success: Boolean = false, notify: Boolean = false, t: T? = null) {
        this.message = message
        this.error = error
        this.success = success
        this.notify = notify
        this.t = t
    }

    companion object {
        fun ok(): AppResponse<Any?> {
            return AppResponse("ok", "", true)
        }

        fun <T>ok(obj: T): AppResponse<T> {
            return AppResponse("ok", "", true, t = obj)
        }

        fun <T>notify(message: String): AppResponse<T> {
            return AppResponse(message, "", true, notify = true)
        }

        fun error(error: String): AppResponse<String> {
            return AppResponse("", error, false)
        }

        fun noneAuth(): AppResponse<*> {
            return AppResponse<Any?>("", "noneAuth", false)
        }
    }
}