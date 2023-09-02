package com.razitulikhlas.banknagari.ui.login

import androidx.lifecycle.ViewModel
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseCheckPhone
import com.razitulikhlas.core.domain.login.usecase.ILoginUseCase
import kotlinx.coroutines.flow.Flow

class LoginViewModel(private val useCase: ILoginUseCase) : ViewModel()  {

    suspend fun checkPhone(phone:String): Flow<ApiResponse<ResponseCheckPhone>>
            = useCase.checkPhone(phone)
}