package com.razitulikhlas.core.data.source.local.client

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(clientEntity: ClientEntity) : Long

    @Query("SELECT * FROM tbl_client WHERE idClient = :id")
    fun getDetail(id: Long): Flow<ClientEntity>

    @Delete
    suspend fun delete(clientEntity: ClientEntity)

//    @Query("SELECT * FROM tbl_customers WHERE id = :id")
//    fun getDetail(id: Int): Flow<CustomerEntity>
//
//    @Query("SELECT * FROM tbl_customers ")
//    fun getCustomer(): Flow<List<CustomerEntity>>
}