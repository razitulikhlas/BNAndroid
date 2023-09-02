package com.razitulikhlas.core.domain.mapping.repository

import com.razitulikhlas.core.data.source.LocalDataSource
import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import kotlinx.coroutines.flow.Flow


class CustomerRepository(private val localDataSource: LocalDataSource):ICustomerRepository {
    override suspend fun save(customerEntity: CustomerEntity) = localDataSource.save(customerEntity)

    override suspend fun delete(customerEntity: CustomerEntity) = localDataSource.delete(customerEntity)

    override fun getCustomer(id: Int): Flow<CustomerEntity> = localDataSource.getCustomer(id)

    override fun getCustomer(): Flow<List<CustomerEntity>> = localDataSource.getCustomer()

    override suspend fun saveDescMap(customerDescMapEntity: CustomerDescMapEntity) = localDataSource.saveCustomerDescMap(customerDescMapEntity)

    override fun getDesc(id: Long): Flow<List<CustomerDescMapEntity>> = localDataSource.getCustomerDescMap(id)
}