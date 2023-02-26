package com.myfood.server.utility.exception

import com.myfood.server.data.models.base.BaseError
import com.myfood.server.data.models.base.BaseResponse

internal class ApplicationException(
    private val errorMessage: String,
) : Throwable(errorMessage) {

    fun toBaseError(): BaseResponse<Unit> {
        return BaseResponse(
            error = BaseError(
                message = errorMessage,
            )
        )
    }
}