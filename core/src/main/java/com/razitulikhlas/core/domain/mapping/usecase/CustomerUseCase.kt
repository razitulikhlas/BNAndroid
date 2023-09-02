package com.razitulikhlas.core.domain.mapping.usecase

import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.domain.mapping.repository.ICustomerRepository
import kotlinx.coroutines.flow.Flow

class CustomerUseCase(private val repository: ICustomerRepository):ICustomerUseCase{
    override suspend fun save(customerEntity: CustomerEntity) = repository.save(customerEntity)

    override suspend fun delete(customerEntity: CustomerEntity) = repository.delete(customerEntity)

    override fun getCustomer(id: Int): Flow<CustomerEntity> = repository.getCustomer(id)

    override fun getCustomer(): Flow<List<CustomerEntity>> = repository.getCustomer()

    override suspend fun saveDescMap(customerDescMapEntity: CustomerDescMapEntity) = repository.saveDescMap(customerDescMapEntity)

    override  fun getDesc(id: Long): Flow<List<CustomerDescMapEntity>> = repository.getDesc(id)

}