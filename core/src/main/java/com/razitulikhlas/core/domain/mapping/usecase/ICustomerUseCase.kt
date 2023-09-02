package com.razitulikhlas.core.domain.mapping.usecase

import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface ICustomerUseCase {
    suspend fun save(customerEntity: CustomerEntity):Long

    suspend fun delete(customerEntity: CustomerEntity)

    fun getCustomer(id:Int): Flow<CustomerEntity>

    fun getCustomer(): Flow<List<CustomerEntity>>

    suspend fun saveDescMap(customerDescMapEntity: CustomerDescMapEntity)

   fun getDesc(id: Long):Flow<List<CustomerDescMapEntity>>
}