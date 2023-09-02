package com.razitulikhlas.core.data.source

import com.razitulikhlas.core.data.source.local.client.ClientDao
import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerDao
import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity

class LocalDataSource(private val customerDao: CustomerDao,private val clientDao: ClientDao) {

    suspend fun save(customerEntity: CustomerEntity) = customerDao.save(customerEntity)

    suspend fun delete(customerEntity: CustomerEntity) = customerDao.delete(customerEntity)

    fun getCustomer(id:Int) = customerDao.getDetail(id)

    fun getCustomer() = customerDao.getCustomer()

    suspend fun saveCustomerDescMap(customerDescMapEntity: CustomerDescMapEntity) = customerDao.saveCustomerDesc(customerDescMapEntity)

    fun getCustomerDescMap(id:Long) = customerDao.getDetailDesc(id)

    fun getDetailClient(id:Long)=clientDao.getDetail(id)

    suspend fun saveClient(clientEntity: ClientEntity) = clientDao.save(clientEntity)

    suspend fun deleteClient(clientEntity: ClientEntity) = clientDao.delete(clientEntity)
}