package com.razitulikhlas.core.util.format

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class RupiahFormat(editText: EditText?) : TextWatcher {
    private var editTextWeakReference: WeakReference<EditText>

    private val locale = Locale("id", "ID")
    private val formatter: DecimalFormat = NumberFormat.getCurrencyInstance(locale) as DecimalFormat

    init {
        editTextWeakReference = WeakReference<EditText>(editText)
        formatter.maximumFractionDigits = 0
        formatter.roundingMode = RoundingMode.FLOOR

        val symbol = DecimalFormatSymbols(locale)
        symbol.currencySymbol = symbol.currencySymbol + " "
        formatter.decimalFormatSymbols = symbol
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference.get()
        if (editText == null || editText.text.toString().isEmpty()) {
            return
        }
        editText.removeTextChangedListener(this)
        val parsed = parseCurrencyValue(editText.text.toString())
        val formatted: String = formatter.format(parsed)


        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }


    private fun parseCurrencyValue(value: String): BigDecimal? {
        try {
            val replaceRegex = java.lang.String.format(
                "[%s,.\\s]",
                Objects.requireNonNull(formatter.currency).getSymbol(locale)
            )
            val currencyValue = value.replace(replaceRegex.toRegex(), "")
            return BigDecimal(currencyValue)
        } catch (e: Exception) {
            Log.e("App", e.message, e)
        }
        return BigDecimal.ZERO
    }




}