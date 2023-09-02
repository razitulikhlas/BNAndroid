package com.razitulikhlas.core.data.source.local.mapping

import androidx.room.Database
import androidx.room.RoomDatabase
import com.razitulikhlas.core.data.source.local.client.ClientDao
import com.razitulikhlas.core.data.source.local.client.ClientEntity

@Database(entities = [CustomerEntity::class,CustomerDescMapEntity::class,ClientEntity::class], version = 3,exportSchema = false)
abstract class DatabaseApps : RoomDatabase() {
    abstract val customerDao : CustomerDao
    abstract val clientDao : ClientDao
}