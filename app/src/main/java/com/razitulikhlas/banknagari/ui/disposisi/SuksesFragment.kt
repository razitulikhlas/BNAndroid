package com.razitulikhlas.banknagari.ui.disposisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.adapter.DataSkimAdapter
import com.razitulikhlas.banknagari.databinding.FragmentSuksesBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardViewModel
import com.razitulikhlas.banknagari.ui.permohonan.InsertApplicationActivity
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.data.storage.DataStoreManager
import com.razitulikhlas.core.util.Constant
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SuksesFragment : Fragment(), DataSkimAdapter.DataSkimCallback {

    private val adapters: DataSkimAdapter by inject()
    private val viewModel : DashBoardViewModel by viewModel()

    private var _binding: FragmentSuksesBinding? = null
    private val binding get() = _binding!!

    private var level = 0
    var info = ""
    var nik = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuksesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun createFragment() {

        if(Constant.LEVEL_CHECK > 2){
            binding.btnAdd.show()
        }else{
            binding.btnAdd.hide()
        }

        adapters.setListener(this@SuksesFragment)
        with(binding){
            rcv.layoutManager = LinearLayoutManager(activity)
            rcv.hasFixedSize()

            btnAdd.setOnClickListener {
                val intent = Intent(requireActivity(), InsertApplicationActivity::class.java)
                intent.putExtra("NIK",nik)
                startActivity(intent)
            }
        }
        Log.e("TAG", "createFragment: ${level}", )

        getData()
        binding.swipe.setOnRefreshListener{
            showLoading()
            getData()
        }

    }

    private fun getData(){

        lifecycleScope.launch() {
            level = DataStoreManager.getIntValue(
                requireActivity(),
                Constant.IS_LEVEL, 0
            )

            Log.e("TAG", "getData: $level", )
            nik = DataStoreManager.getStringValue(
                requireActivity(),
                Constant.IS_ID_USER, ""
            )
            info = DataStoreManager.getStringValue(
                requireActivity(),
                Constant.IS_ID_USER, ""
            )
            if(level < 2){
                if(activity?.intent!!.getStringExtra("skim") != null){
                    info =activity?.intent!!.getStringExtra("skim")!!
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

    private fun showLoading() {
        with(binding) {
            rcv.visibility = View.GONE
            shimmers.visibility = View.VISIBLE
            shimmers.startShimmer()
        }
    }

    override fun onClick(item: DataItemSkim) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}