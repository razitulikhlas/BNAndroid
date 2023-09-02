package com.razitulikhlas.banknagari.ui.disposisi

import com.razitulikhlas.core.data.source.remote.response.DataItemSkim

interface DataCallBack {
    fun sendData(list: List<DataItemSkim>?)
}