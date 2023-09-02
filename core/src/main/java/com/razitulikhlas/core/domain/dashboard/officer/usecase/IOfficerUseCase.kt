package com.razitulikhlas.core.domain.dashboard.officer.usecase

import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IOfficerUseCase {
    suspend fun insertDisposisi(skim: DataItemSkim): Flow<ApiResponse<ResponseDataSkim>>
    suspend fun updateStatusDisposisi(id:Int,status:Int,informasi:String): Flow<ApiResponse<ResponseNormal>>
    suspend fun saveClient(clientEntity: ClientEntity) : Long
    fun getDetailClient(clientEntity: Long) : Flow<ClientEntity>
    suspend fun insertInfoUsaha(
        dataImage: DataImage,
        image1: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image4: MultipartBody.Part?,
        image5: MultipartBody.Part?,
        image6: MultipartBody.Part?,
        image7: MultipartBody.Part?,
        image8: MultipartBody.Part?,
    ): Flow<ApiResponse<ResponseInsertInfoUsaha>>


}