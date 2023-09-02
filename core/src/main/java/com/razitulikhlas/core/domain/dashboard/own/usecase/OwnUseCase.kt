package com.razitulikhlas.core.domain.dashboard.own.usecase

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseData
import com.razitulikhlas.core.data.source.remote.response.ResponseDataSkim
import com.razitulikhlas.core.domain.dashboard.own.repository.IOwnRepository
import kotlinx.coroutines.flow.Flow

class OwnUseCase(private val repository: IOwnRepository):IOwnUseCase {
    override suspend fun getDataSkim(skim: String,level:Int): Flow<ApiResponse<ResponseDataSkim>>
    = repository.getDataSkim(skim,level)

    override suspend fun getData(): Flow<ApiResponse<ResponseData>> = repository.getData()

}