package com.razitulikhlas.banknagari.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableInstallment(
    var no:Int,
    var installmentPrimary:Double,//angsuran pokok
    var bankInterest:Double,// bunga Ansuran,
    var installmentAmount:Double,//total angsuran
    var remainingLoan:Double,//sisa angsuran,
) : Parcelable
