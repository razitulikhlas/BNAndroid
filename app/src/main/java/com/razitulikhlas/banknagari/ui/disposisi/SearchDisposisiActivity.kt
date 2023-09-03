package com.razitulikhlas.banknagari.ui.disposisi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.e
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.adapter.DataSkimAdapter
import com.razitulikhlas.banknagari.databinding.ActivityHomeDisposisiBinding
import com.razitulikhlas.banknagari.databinding.ActivitySearchDisposisiBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardViewModel
import com.razitulikhlas.banknagari.ui.permohonan.OfficerViewModel
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDisposisiActivity : AppCompatActivity() {

    private val viewModel : OfficerViewModel by viewModel()
    lateinit var binding : ActivitySearchDisposisiBinding
    private val adapters: DataSkimAdapter by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchDisposisiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edUsaha.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    viewModel.queryChannel.send(s.toString())
                }
            }
        })

        viewModel.searchResult.observe(this) { film ->
            film.observe(this) { resource ->
                if (resource != null) {
                    when (resource) {
                        is ApiResponse.Success -> {
                            resource.data?.let {
                                e("search",it.data?.size.toString())
                                 with(binding.rcv){
                                     layoutManager = LinearLayoutManager(this@SearchDisposisiActivity)
                                     adapters.setData(it.data!!)
                                     adapter = adapters
                                     hasFixedSize()
                                 }
                                showPreviewData()
//                                moviesAdapter?.setData(it)
//                                binding.recyclerView.adapter = moviesAdapter
                            }
                        }
                        is ApiResponse.Error -> {
//                            moviesAdapter?.setData(null)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showPreviewData() {
        with(binding) {
            rcv.visibility = View.VISIBLE
            shimmers.visibility = View.GONE
            shimmers.stopShimmer()
        }
    }
}