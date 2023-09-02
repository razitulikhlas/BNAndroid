package com.razitulikhlas.core.domain.dashboard.own.repository

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseData
import com.razitulikhlas.core.data.source.remote.response.ResponseDataSkim
import kotlinx.coroutines.flow.Flow

interface IOwnRepository {
    suspend fun getDataSkim(skim:String,level:Int): Flow<ApiResponse<ResponseDataSkim>>
    suspend fun getData():Flow<ApiResponse<ResponseData>>
}