package com.myfood.server.data.models.base

import com.myfood.server.utility.constant.ResponseKeyConstant

@kotlinx.serialization.Serializable
data class BaseResponse<T>(
    var version: String = ResponseKeyConstant.VERSION,
    var status: String = ResponseKeyConstant.ERROR,
    var result: T? = null,
    var error: BaseError? = null,
)