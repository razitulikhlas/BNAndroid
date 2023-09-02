package com.razitulikhlas.core.data.source.local.mapping

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long ? = null,
    @ColumnInfo(name = "businessName")
    var businessName: String? = null,
    @ColumnInfo(name = "ownerName")
    var ownerName: String? = null,
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @ColumnInfo(name = "loanStatus")
    var loanStatus: String? = null,
    @ColumnInfo(name = "responseStatus")
    var responseStatus: String? = null,
    @ColumnInfo(name = "platFond")
    var platFond: String? = null,
    @ColumnInfo(name = "bakidebet")
    var bakidebet: String? = null,
    @ColumnInfo(name = "informationBusiness")
    var informationBusiness: String? = null,
    @ColumnInfo(name = "photo")
    var photo:String?=null,
    @ColumnInfo(name = "address")
    var address:String? = null,
    @ColumnInfo(name = "latitude")
    var latitude:Double? = null,
    @ColumnInfo(name = "longitude")
    var longitude:Double? = null,
) : Parcelable