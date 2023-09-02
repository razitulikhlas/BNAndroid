package com.razitulikhlas.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseNormal(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
