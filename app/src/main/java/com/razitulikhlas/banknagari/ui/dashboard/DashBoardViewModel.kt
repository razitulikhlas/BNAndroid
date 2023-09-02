package com.razitulikhlas.banknagari.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.data.source.remote.response.ResponseData
import com.razitulikhlas.core.domain.dashboard.own.usecase.IOwnUseCase
import kotlinx.coroutines.flow.Flow

class DashBoardViewModel(private val useCase: IOwnUseCase) : ViewModel()  {

    val dataSkimProses = MutableLiveData<List<DataItemSkim>>()
    val dataSkimCancel = MutableLiveData<List<DataItemSkim>>()
    val dataSkimSuccess = MutableLiveData<List<DataItemSkim>>()

    suspend fun getData():Flow<ApiResponse<ResponseData>>{
        return useCase.getData()
    }

    fun getSuccess():LiveData<List<DataItemSkim>>{
        return dataSkimSuccess
    }

    fun getProses():LiveData<List<DataItemSkim>>{
        return dataSkimProses
    }

    fun getCancel():LiveData<List<DataItemSkim>>{
        return dataSkimCancel
    }

    suspend fun getDataSkim(skim:String,level:Int){
        useCase.getDataSkim(skim,level).asLiveData().observeForever { it ->
            when(it){
                is ApiResponse.Success->{
                    if(it.data.data != null){
                        val p = it.data.data?.filter {p->p.status == 1 }
                        dataSkimProses.value = p!!
                        val c = it.data.data?.filter {c->c.status == 0 }
                        dataSkimCancel.value = c!!
                        val s = it.data.data?.filter {s->s.status == 2 }
                        dataSkimSuccess.value = s!!
                        Log.e("viewmodel", "P: $p", )
                        Log.e("viewmodel", "C: $c", )
                        Log.e("viewmodel", "S: ${dataSkimSuccess.value}", )
                    }
                }
                is ApiResponse.Error->{

                }
                is ApiResponse.Empty->{

                }
            }
        }
    }



}