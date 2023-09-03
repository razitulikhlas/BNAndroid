package com.razitulikhlas.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseSearch(

	@field:SerializedName("data")
	val data: List<DataItemSkim>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataSearch(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("ktp_penjamin")
	val ktpPenjamin: String? = null,

	@field:SerializedName("skim_kredit")
	val skimKredit: String? = null,

	@field:SerializedName("plafond")
	val plafond: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("is_delete")
	val isDelete: Int? = null,

	@field:SerializedName("penjamin")
	val penjamin: String? = null,

	@field:SerializedName("sektor_usaha")
	val sektorUsaha: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("pemohon")
	val pemohon: String? = null,

	@field:SerializedName("jangka_waktu")
	val jangkaWaktu: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("ktp_pemohon")
	val ktpPemohon: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable
