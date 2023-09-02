package com.razitulikhlas.core.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


val Context.settingDataStore: DataStore<androidx.datastore.preferences.core.Preferences>  by preferencesDataStore(name = "settings")

object DataStoreManager {

    suspend fun saveValue(context: Context,key:String,value: Boolean) = withContext(Dispatchers.IO){
        val wrappedKey = booleanPreferencesKey(key)
        context.settingDataStore.edit {
            it[wrappedKey] = value
        }
    }

    suspend fun saveValue(context: Context,key:String,value: String) = withContext(Dispatchers.IO){
        val wrappedKey = stringPreferencesKey(key)
        context.settingDataStore.edit {
            it[wrappedKey] = value
        }
    }

    suspend fun saveValue(context: Context,key:String,value: Int) = withContext(Dispatchers.IO){
        val wrappedKey = intPreferencesKey(key)
        context.settingDataStore.edit {
            it[wrappedKey] = value
        }
    }

    suspend fun getBooleanValue(context: Context,key:String,default :Boolean = false):Boolean = withContext(
        Dispatchers.IO){
        val wrappedKey = booleanPreferencesKey(key)
        val valueFlow : Flow<Boolean> = context.settingDataStore.data.map {
            it[wrappedKey]?:default
        }
        return@withContext valueFlow.first()
    }

    suspend fun getStringValue(context: Context,key:String,default :String = ""):String = withContext(
        Dispatchers.IO){
        val wrappedKey = stringPreferencesKey(key)
        val valueFlow : Flow<String> = context.settingDataStore.data.map {
            it[wrappedKey]?:default
        }
        return@withContext valueFlow.first()
    }

    suspend fun getIntValue(context: Context,key:String,default :Int = 0):Int = withContext(
        Dispatchers.IO){
        val wrappedKey = intPreferencesKey(key)
        val valueFlow : Flow<Int> = context.settingDataStore.data.map {
            it[wrappedKey]?:default
        }
        return@withContext valueFlow.first()
    }

}



