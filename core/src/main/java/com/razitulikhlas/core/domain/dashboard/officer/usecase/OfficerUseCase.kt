package com.razitulikhlas.core.domain.dashboard.officer.usecase

import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.*
import com.razitulikhlas.core.domain.dashboard.officer.repository.IOfficerRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class OfficerUseCase(val repository: IOfficerRepository) :IOfficerUseCase{
    override suspend fun insertDisposisi(skim: DataItemSkim): Flow<ApiResponse<ResponseDataSkim>>
     =repository.insertDisposisi(skim)

    override suspend fun updateStatusDisposisi(
        id:Int,
        status: Int,
        informasi: String
    ): Flow<ApiResponse<ResponseNormal>> = repository.updateStatusDisposisi(id,status,informasi)

    override suspend fun saveClient(clientEntity: ClientEntity): Long = repository.saveClient(clientEntity)

    override fun getDetailClient(clientEntity: Long): Flow<ClientEntity> = repository.getDetailClient(clientEntity)
    override suspend fun insertInfoUsaha(
        dataImage: DataImage,
        image1: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image4: MultipartBody.Part?,
        image5: MultipartBody.Part?,
        image6: MultipartBody.Part?,
        image7: MultipartBody.Part?,
        image8: MultipartBody.Part?
    ): Flow<ApiResponse<ResponseInsertInfoUsaha>>
    =repository.insertInfoUsaha(
        dataImage,image1,image2,image3,image4,
        image5,image6,image7,image8
    )

    override suspend fun search(pemohon: String): Flow<ApiResponse<ResponseSearch>> = repository.search(pemohon)
}