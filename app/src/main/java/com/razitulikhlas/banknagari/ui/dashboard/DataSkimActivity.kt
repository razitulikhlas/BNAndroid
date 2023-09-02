package com.razitulikhlas.banknagari.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.adapter.DataSkimAdapter
import com.razitulikhlas.banknagari.databinding.ActivityDataSkimBinding
import com.razitulikhlas.banknagari.ui.disposisi.DetailDisposisiActivity
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DataSkimActivity : AppCompatActivity(), DataSkimAdapter.DataSkimCallback {
    lateinit var binding : ActivityDataSkimBinding
    private val adapter: DataSkimAdapter by inject()
    private val viewModel : DashBoardViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDataSkimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val skim = intent.getStringExtra("skim")
        with(binding){
            rcv.layoutManager = LinearLayoutManager(this@DataSkimActivity)
            rcv.setHasFixedSize(true)
        }
        adapter.setListener(this)
        showLoading()
        lifecycleScope.launch {
            delay(1000)
//            viewModel.getDataSkim(skim!!).asLiveData().observeForever {
//                when(it){
//                    is ApiResponse.Success->{
//                        adapter.setData(it.data.data!!)
//                        binding.rcv.adapter = adapter
//                    }
//                    is ApiResponse.Error->{
//
//                    }
//                    else->{
//
//                    }
//                }
//                showPreviewData()
//            }
        }
    }

    private fun showPreviewData() {
        with(binding) {
            rcv.visibility = View.VISIBLE
            shimmers.visibility = View.GONE
            shimmers.stopShimmer()
        }
    }

    private fun showLoading() {
        with(binding) {
            rcv.visibility = View.GONE
            shimmers.visibility = View.VISIBLE
            shimmers.startShimmer()
        }
    }

    override fun onClick(item: DataItemSkim) {
        val intent = Intent(this,DetailDisposisiActivity::class.java)
        intent.putExtra("data",item)
        startActivity(intent)
    }

//    override fun onClickCall(phone: String) {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),10)
//            return
//        }else{
//            val uri = "tel:" + phone.trim()
//            val intent = Intent(Intent.ACTION_CALL)
//            intent.data = Uri.parse(uri)
//            startActivity(intent)
//        }
//    }
}