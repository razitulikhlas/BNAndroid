package com.razitulikhlas.core.domain.dashboard.officer.repository

import com.razitulikhlas.core.data.source.LocalDataSource
import com.razitulikhlas.core.data.source.RemoteDataSource
import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class OfficerRepository(val remoteDataSource: RemoteDataSource,val localDataSource: LocalDataSource):IOfficerRepository {
    override suspend fun insertDisposisi(skim: DataItemSkim): Flow<ApiResponse<ResponseDataSkim>>
        =remoteDataSource.insertDisposisi(skim)

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
    ): Flow<ApiResponse<ResponseInsertInfoUsaha>> =
        remoteDataSource.insertInfoUsaha(
            dataImage,image1,image2,image3,image4,
            image5,image6,image7,image8)

    override suspend fun updateStatusDisposisi(
        id:Int,
        status: Int,
        informasi: String
    ): Flow<ApiResponse<ResponseNormal>> =remoteDataSource.updateStatusDisposisi(id,status, informasi)

    override suspend fun saveClient(clientEntity: ClientEntity): Long = localDataSource.saveClient(clientEntity)

    override fun getDetailClient(clientEntity: Long): Flow<ClientEntity> = localDataSource.getDetailClient(clientEntity)

    override suspend fun search(pemohon: String): Flow<ApiResponse<ResponseSearch>> = remoteDataSource.search(pemohon)
    override suspend fun detailDisposisi(id: Int): Flow<ApiResponse<ResponseDetailDisposisi>>  = remoteDataSource.detailDisposisi(id)
}