package com.razitulikhlas.core.domain.login.usecase

import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseCheckPhone
import com.razitulikhlas.core.domain.login.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: ILoginRepository):ILoginUseCase {
    override suspend fun checkPhone(phone: String): Flow<ApiResponse<ResponseCheckPhone>>
        =repository.checkPhone(phone)
}