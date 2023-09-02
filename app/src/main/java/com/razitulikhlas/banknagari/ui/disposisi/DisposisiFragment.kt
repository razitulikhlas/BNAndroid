package com.razitulikhlas.banknagari.ui.disposisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.adapter.DataSkimAdapter
import com.razitulikhlas.banknagari.databinding.FragmentDisposisiBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardViewModel
import com.razitulikhlas.banknagari.ui.permohonan.InsertApplicationActivity
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.util.Constant.LEVEL_CHECK
import org.koin.android.ext.android.inject


class DisposisiFragment : Fragment(), DataSkimAdapter.DataSkimCallback{

    private val adapters: DataSkimAdapter by inject()
    private val viewModel : DashBoardViewModel by activityViewModels()

    private var _binding: FragmentDisposisiBinding? = null
    private val binding get() = _binding!!

    var info = ""


    companion object {
        const val CATEGORY = "category"
        fun newInstance(category: String?): DisposisiFragment{
            val args = Bundle()
            args.putSerializable(CATEGORY, category)
            val fragment = DisposisiFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentDisposisiBinding.inflate(layoutInflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragment()
    }

    private fun createFragment() {
        Log.e("LEVEL", "createFragment: $LEVEL_CHECK", )

        if(LEVEL_CHECK> 2){
            binding.btnAdd.show()
        }else{
            binding.btnAdd.hide()
        }

        adapters.setListener(this@DisposisiFragment)
        with(binding){
            btnAdd.setOnClickListener {
                val intent = Intent(requireActivity(), InsertApplicationActivity::class.java)
                startActivity(intent)
            }
        }
        binding.swipe.setOnRefreshListener{
            binding.swipe.isRefreshing = false

//            showLoading()
//            lifecycleScope.launch {
//                var info = Constant.NIK_CHECK
//                if(LEVEL_CHECK < 2){
//                    if(activity?.intent!!.getStringExtra("skim") != null){
//                        info =activity?.intent!!.getStringExtra("skim")!!
//                    }
//                }
//                viewModel.getDataSkim(info,LEVEL_CHECK)
//            }
//            getData()
        }
        getData()
    }

    private fun getData(){
        val category = arguments?.getString(CATEGORY)
        if(category == "cancel"){
            Log.e("TAG", "CANCEL: ", )
            with(binding.rcv){
                viewModel.getCancel().observe(viewLifecycleOwner){
                    Log.e("CANCEL", "getData: $it", )
                    layoutManager = LinearLayoutManager(activity)
                    adapters.setData(it)
                    adapter = adapters
                    hasFixedSize()
                }
            }

        }else if(category == "proses"){
            with(binding.rcv){
                viewModel.getProses().observe(viewLifecycleOwner){
                    Log.e("PROSES", "getData: $it", )
                    layoutManager = LinearLayoutManager(activity)
                    adapters.setData(it)
                    adapter = adapters
                    hasFixedSize()
                }
            }
        }else if(category == "selesai"){
            with(binding.rcv){
                viewModel.getSuccess().observe(viewLifecycleOwner){
                    Log.e("SELESAI", "getData: $it", )
                    layoutManager = LinearLayoutManager(activity)
                    adapters.setData(it)
                    adapter = adapters
                    hasFixedSize()
                }
            }
        }
        showPreviewData()
    }

    private fun showPreviewData() {
        with(binding) {
            swipe.isRefreshing=false
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
       val intent  = Intent(activity,DetailDisposisiActivity::class.java)
        intent.putExtra("data",item)
        requireActivity().startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}