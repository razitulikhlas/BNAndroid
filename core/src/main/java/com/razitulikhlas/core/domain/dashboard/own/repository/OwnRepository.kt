package com.razitulikhlas.core.domain.dashboard.own.repository

import com.razitulikhlas.core.data.source.RemoteDataSource
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseData
import com.razitulikhlas.core.data.source.remote.response.ResponseDataSkim
import kotlinx.coroutines.flow.Flow

class OwnRepository(private val remoteDataSource: RemoteDataSource):IOwnRepository {
    override suspend fun getDataSkim(skim: String,level:Int): Flow<ApiResponse<ResponseDataSkim>>
             =remoteDataSource.getDataSkim(skim,level)

    override suspend fun getData(): Flow<ApiResponse<ResponseData>>
             = remoteDataSource.getData()
}