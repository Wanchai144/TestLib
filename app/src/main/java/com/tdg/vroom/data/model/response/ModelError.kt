package com.tdg.vroom.data.model.response

import com.google.gson.annotations.SerializedName

data class ModelError(
    @SerializedName("error")
    val error: String?,
    @SerializedName("error_description")
    val errorDescription: String?
)