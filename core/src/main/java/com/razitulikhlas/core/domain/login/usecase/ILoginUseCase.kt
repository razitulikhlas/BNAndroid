package com.razitulikhlas.core.domain.login.usecase

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseCheckPhone
import kotlinx.coroutines.flow.Flow

interface ILoginUseCase {
    suspend fun checkPhone(phone:String): Flow<ApiResponse<ResponseCheckPhone>>

}