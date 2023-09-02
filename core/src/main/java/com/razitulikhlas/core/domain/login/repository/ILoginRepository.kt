package com.razitulikhlas.core.domain.login.repository

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseCheckPhone
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {
    suspend fun checkPhone(phone:String): Flow<ApiResponse<ResponseCheckPhone>>

}