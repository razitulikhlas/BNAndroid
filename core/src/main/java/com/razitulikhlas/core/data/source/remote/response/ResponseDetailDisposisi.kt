package com.razitulikhlas.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseDetailDisposisi(

	@field:SerializedName("data")
	val data: DataDetailDisposisi? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("kode_cabang")
	val kodeCabang: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable

@Parcelize
data class DataDetailDisposisi(

	@field:SerializedName("infousaha")
	val infousaha: InfoBusiness? = null,

	@field:SerializedName("inforumah")
	val inforumah: InfoHouse? = null,

	@field:SerializedName("disposisi")
	val disposisi: Disposisi? = null
) : Parcelable

@Parcelize
data class Disposisi(

	@field:SerializedName("name_ao")
	val nameAo: String? = null,

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
	val idUser: String? = null,

	@field:SerializedName("is_delete")
	val isDelete: String? = null,

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
	val jangkaWaktu: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("ktp_pemohon")
	val ktpPemohon: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class InfoBusiness(

	@field:SerializedName("image5")
	val image5: String? = null,

	@field:SerializedName("image6")
	val image6: String? = null,

	@field:SerializedName("image3")
	val image3: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("image4")
	val image4: String? = null,

	@field:SerializedName("image7")
	val image7: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("image8")
	val image8: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("image1")
	val image1: String? = null,

	@field:SerializedName("image2")
	val image2: String? = null,

	@field:SerializedName("id_disposisi")
	val idDisposisi: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("longititude")
	val longititude: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable

@Parcelize
data class InfoHouse(

	@field:SerializedName("image5")
	val image5: String? = null,

	@field:SerializedName("image6")
	val image6: String? = null,

	@field:SerializedName("image3")
	val image3: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("image4")
	val image4: String? = null,

	@field:SerializedName("image7")
	val image7: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("image8")
	val image8: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("image1")
	val image1: String? = null,

	@field:SerializedName("image2")
	val image2: String? = null,

	@field:SerializedName("id_disposisi")
	val idDisposisi: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("longititude")
	val longititude: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable


