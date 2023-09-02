package com.razitulikhlas.banknagari.ui.permohonan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.*
import com.razitulikhlas.core.domain.dashboard.officer.usecase.IOfficerUseCase
import okhttp3.MultipartBody

class OfficerViewModel (private val useCase: IOfficerUseCase) : ViewModel() {
    suspend fun insert(skim:DataItemSkim): LiveData<ApiResponse<ResponseDataSkim>>
            = useCase.insertDisposisi(skim).asLiveData()

    suspend fun updateStatusDisposisi(id:Int,status:Int,informasi:String): LiveData<ApiResponse<ResponseNormal>> =
        useCase.updateStatusDisposisi(id,status, informasi).asLiveData()

    suspend fun save(clientEntity: ClientEntity) = useCase.saveClient(clientEntity)

    suspend fun insertInfoUsaha(
                    dataImage: DataImage,
                     image1: MultipartBody.Part?,
                     image2: MultipartBody.Part?,
                     image3: MultipartBody.Part?,
                     image4: MultipartBody.Part?,
                     image5: MultipartBody.Part?,
                     image6: MultipartBody.Part?,
                     image7: MultipartBody.Part?,
                     image8: MultipartBody.Part?
    ):LiveData<ApiResponse<ResponseInsertInfoUsaha>>
    =useCase.insertInfoUsaha(
        dataImage,image1,image2,image3,image4,
        image5,image6,image7,image8
    ).asLiveData()

    suspend fun getClientId(id:Long):LiveData<ClientEntity> = useCase.getDetailClient(id).asLiveData()
}