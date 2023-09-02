package com.razitulikhlas.core.data.source.local.mapping


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(customerEntity: CustomerEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomerDesc(customerDescMap: CustomerDescMapEntity)

    @Query("SELECT * FROM tbl_customer_desc_map WHERE idCustomer = :id")
    fun getDetailDesc(id: Long): Flow<List<CustomerDescMapEntity>>

    @Delete
    suspend fun delete(customerEntity: CustomerEntity)

    @Query("SELECT * FROM tbl_customers WHERE id = :id")
    fun getDetail(id: Int): Flow<CustomerEntity>

    @Query("SELECT * FROM tbl_customers ")
    fun getCustomer(): Flow<List<CustomerEntity>>
}