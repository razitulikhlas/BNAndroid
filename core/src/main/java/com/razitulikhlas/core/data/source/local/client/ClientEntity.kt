package com.razitulikhlas.core.data.source.local.client

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_client")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long ? = null,
    @ColumnInfo(name = "idClient")
    var idClient: Long? = null,
    @ColumnInfo(name = "ownerName")
    var ownerName: String? = null,
    @ColumnInfo(name = "photo")
    var photo:String?=null,
    @ColumnInfo(name = "address")
    var address:String? = null,
    @ColumnInfo(name = "latitude")
    var latitude:Double? = null,
    @ColumnInfo(name = "longitude")
    var longitude:Double? = null,
) : Parcelable