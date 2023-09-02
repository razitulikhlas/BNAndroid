package com.razitulikhlas.core.data.source.local.mapping

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "tbl_customer_desc_map")
data class CustomerDescMapEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long ? = null,
    @ColumnInfo(name = "idCustomer")
    var idCustomer: Long ? = null,
    @ColumnInfo(name = "date")
    var date: String? = null,
    @ColumnInfo(name = "desc")
    var desc: String? = null,
    @ColumnInfo(name = "expanded")
    var expanded : Boolean = false
): Parcelable