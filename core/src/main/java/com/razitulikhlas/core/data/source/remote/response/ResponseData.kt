package com.razitulikhlas.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseData(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("total")
    val total: Double? = null,

	@field:SerializedName("selesai")
	val selesai: Selesai? = null,

	@field:SerializedName("cancel")
	val cancel: Cancel? = null,

	@field:SerializedName("proses")
	val proses: Proses? = null

)

data class Cancel(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("total_plafond")
	val totalPlafond: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Proses(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("total_plafond")
	val totalPlafond: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Selesai(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("total_plafond")
	val totalPlafond: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItem(

	@field:SerializedName("jumlah")
	val jumlah: String? = null,

	@field:SerializedName("skim_kredit")
	val skimKredit: String? = null,

	@field:SerializedName("total_plafond")
	val totalPlafond: String? = null
)
