package com.razitulikhlas.banknagari.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.domain.mapping.usecase.ICustomerUseCase
import com.razitulikhlas.core.util.getCurrentDate

class CustomerViewModel(private val useCase: ICustomerUseCase):ViewModel(){

    suspend fun save(customerEntity: CustomerEntity) {
        val id = useCase.save(customerEntity)
        val date = getCurrentDate()
        val cs = CustomerDescMapEntity(null,id,date,customerEntity.informationBusiness)
        saveDesc(cs)
    }

    suspend fun saveDesc(customerDescMapEntity: CustomerDescMapEntity)=useCase.saveDescMap(customerDescMapEntity)

    suspend fun delete(customerEntity: CustomerEntity) = useCase.delete(customerEntity)

    fun getCustomer(id:Int):LiveData<CustomerEntity> = useCase.getCustomer(id).asLiveData()

    fun getCustomer():LiveData<List<CustomerEntity>> = useCase.getCustomer().asLiveData()

    fun getCustomerDescMap(id: Long) = useCase.getDesc(id).asLiveData()
}