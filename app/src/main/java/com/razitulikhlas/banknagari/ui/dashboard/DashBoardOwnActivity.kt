package com.razitulikhlas.banknagari.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.adapter.DashboardSkimAdapter
import com.razitulikhlas.banknagari.databinding.ActivityDashBoardOwnBinding
import com.razitulikhlas.banknagari.ui.disposisi.HomeDisposisiActivity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.util.formatRupiah
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashBoardOwnActivity : AppCompatActivity(), DashboardSkimAdapter.DashboardSkimCallback {
    lateinit var binding : ActivityDashBoardOwnBinding
    private val adapter: DashboardSkimAdapter by inject()
    private val viewModel : DashBoardViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashBoardOwnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcv.layoutManager = LinearLayoutManager(this)

        adapter.setListener(this)

        binding.swipe.setOnRefreshListener {
            lifecycleScope.launch {
                showLoading()
                delay(2000)
                viewModel.getData().asLiveData().observeForever { it ->
                    when(it){
                        is ApiResponse.Success->{
                            adapter.setData(it.data.data!!)
                            binding.tvPladfond.text = formatRupiah( it.data.total!!.toDouble())
                            binding.rcv.adapter = adapter
                            with(binding){
                                if(it.data.cancel == null){
                                    tvCancel.text = "0"
                                }else if(it.data.proses == null){
                                    tvProses.text = "0"
                                }else if(it.data.selesai == null){
                                    tvSukses.text = "0"
                                }
                                it.data.proses?.let {p->
                                    p.let { p1->
                                        tvProses.text = formatRupiah(p1.totalPlafond!!.toDouble())
                                    }
                                }
                                it.data.cancel?.let {c->
                                    c.let {c1->
                                        tvCancel.text = formatRupiah(c1.totalPlafond!!.toDouble())
                                    }
                                }
                                it.data.selesai?.let {s->
                                    s.let {s1->
                                        tvSukses.text = formatRupiah(s1.totalPlafond!!.toDouble())
                                    }
                                }

                            }
                        }
                        is ApiResponse.Error->{
                            Log.e("TAG", "onCreate: ${it.errorMessage}", )
                        }
                        else->{

                        }
                    }
                    showPreviewData()
                    binding.swipe.isRefreshing = false
                }

            }
        }




        lifecycleScope.launch {
            showLoading()
            delay(2000)
            viewModel.getData().asLiveData().observeForever {
                when(it){
                    is ApiResponse.Success->{
                        with(binding){
                            if(it.data.cancel == null){
                                tvCancel.text = "0"
                            }else if(it.data.proses == null){
                                tvProses.text = "0"
                            }else if(it.data.selesai == null){
                                tvSukses.text = "0"
                            }
                            it.data.proses?.let {p->
                                p.let { p1->
                                    tvProses.text = formatRupiah(p1.totalPlafond!!.toDouble())
                                }
                            }
                            it.data.cancel?.let {c->
                                c.let {c1->
                                    tvCancel.text = formatRupiah(c1.totalPlafond!!.toDouble())
                                }
                            }
                            it.data.selesai?.let {s->
                                s.let {s1->
                                    tvSukses.text = formatRupiah(s1.totalPlafond!!.toDouble())
                                }
                            }
                        }
                        Log.e("TAG", "onCreate: ${it.data.data}", )
                        adapter.setData(it.data.data!!)
                        binding.tvPladfond.text = formatRupiah( it.data.total!!.toDouble())
                        binding.rcv.adapter = adapter
                    }
                    is ApiResponse.Error->{
                        Log.e("TAG", "onCreate: ${it.errorMessage}", )
                    }
                    else->{

                    }
                }
                showPreviewData()
            }

        }
    }

    private fun showPreviewData() {
        with(binding) {
            view.visibility = View.VISIBLE
            shimmers.visibility = View.GONE
            shimmers.stopShimmer()
        }
    }

    private fun showLoading() {
        with(binding) {
            view.visibility = View.GONE
            shimmers.visibility = View.VISIBLE
            shimmers.startShimmer()
        }
    }

    override fun onClick(skim: String) {
        val intent = Intent(this,HomeDisposisiActivity::class.java)
        intent.putExtra("skim",skim)
        startActivity(intent)
    }


}