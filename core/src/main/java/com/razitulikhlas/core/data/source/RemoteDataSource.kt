package com.razitulikhlas.core.data.source

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.network.ApiService
import com.razitulikhlas.core.data.source.remote.response.*
import com.razitulikhlas.core.util.createPartFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody


class RemoteDataSource(private val apiService: ApiService){


    suspend fun getData() : Flow<ApiResponse<ResponseData>>{
        return flow {
            try {
                val response = apiService.getData()

                if (response.success == true) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getDataSkim(skim:String,level:Int):Flow<ApiResponse<ResponseDataSkim>>{
        return flow{
            try{
                val response = apiService.getDataSkim(skim,level)

                if(response.success == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            } catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun checkPhone(phone:String):Flow<ApiResponse<ResponseCheckPhone>>{
        return flow{
            try {
                val response = apiService.checkPhone(phone)

                if(response.success == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertDisposisi(item:DataItemSkim):Flow<ApiResponse<ResponseDataSkim>>{
        return flow{
            try{
                val response = apiService.insert(
                    item.idUser!!.toInt(),
                    item.pemohon!!,
                    item.ktpPemohon!!,
                    item.penjamin!!,
                    item.ktpPenjamin!!,
                    item.phone!!,
                    item.skimKredit!!,
                    item.sektorUsaha!!,
                    item.plafond!!.toInt(),
                    item.jangkaWaktu!!.toInt()
                )

                if(response.success == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            } catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateStatusDisposisi(id:Int,status:Int,keterangan:String):Flow<ApiResponse<ResponseNormal>>{
        return flow{
            try{
                val response = apiService.updateStatusDisposisi(
                    id,status,keterangan
                )
                if(response.success == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            } catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertInfoUsaha(dataImage: DataImage,
                                image1:MultipartBody.Part?,
                                image2:MultipartBody.Part?,
                                image3:MultipartBody.Part?,
                                image4:MultipartBody.Part?,
                                image5:MultipartBody.Part?,
                                image6:MultipartBody.Part?,
                                image7:MultipartBody.Part?,
                                image8:MultipartBody.Part?,
    ):Flow<ApiResponse<ResponseInsertInfoUsaha>>{
        return flow {
            try {
                val response = apiService.insertInfoUsaha(
                    createPartFromString(dataImage.idDisposisi!!),
                    createPartFromString(dataImage.latitude!!),
                    createPartFromString(dataImage.longititude!!),
                    createPartFromString(dataImage.address!!),
                    image1 ,
                    image2,
                    image3,
                    image4,
                    image5,
                    image6,
                    image7,
                    image8,
                )
                if(response.success == true){
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch(e:Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun search(s: String) : Flow<ApiResponse<ResponseSearch>>{
        return flow {
            try {
                val response = apiService.search(s)
                if (response.success == true) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }




}