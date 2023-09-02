package com.razitulikhlas.core.util

import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

object Constant {
    const val TYPE_CALL_MAPPING = 1
    const val TYPE_INFO_MAPPING = 2
    const val DATA_MAPPING = "Data Mapping"
    const val ID_MAPPING = "ID Mapping"
    const val IS_LOGIN = "LOGIN"
    const val IS_NAME = "NAME"
    const val IS_ID_USER = "NIK"
    const val IS_PHOTO = "PHOTO"
    const val IS_KODE_CABANG = "KODE_CABANG"
    const val IS_LEVEL = "LEVEL"
    var LEVEL_CHECK =0
    var NIK_CHECK =""
//    var LEVEL_CHECK =0

    fun rupiah(number: Double): String{
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number.roundToInt()).toString().dropLast(3)
    }
}