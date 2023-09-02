package com.razitulikhlas.banknagari.ui.disposisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.razitulikhlas.banknagari.adapter.SectionsPagerAdapter
import com.razitulikhlas.banknagari.databinding.ActivityHomeDisposisiBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardViewModel
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.util.Constant
import com.razitulikhlas.core.util.Constant.LEVEL_CHECK
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeDisposisiActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeDisposisiBinding
    private val viewModel : DashBoardViewModel by viewModel()
    var dataSukses = ArrayList<DataItemSkim>()
    private val listFragment = arrayListOf(
        DisposisiFragment.newInstance("cancel"),
        DisposisiFragment.newInstance("proses"),
        DisposisiFragment.newInstance("selesai"),
    )
    private var mediator: TabLayoutMediator? = null
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityHomeDisposisiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            Log.e("TAG", "onCreate: klik", )
            startActivity(Intent(this,SearchDisposisiActivity::class.java))
        }

        sectionsPagerAdapter =
            SectionsPagerAdapter(lifecycle, supportFragmentManager,listFragment)
        binding.viewPager.adapter = sectionsPagerAdapter
        mediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "cancel"
            } else if (position == 1) {
                tab.text = "diproses"
            }else if (position == 2) {
                tab.text = "selesai"
            }
        }

        mediator?.attach()

        lifecycleScope.launch {
            var info = Constant.NIK_CHECK
            if(LEVEL_CHECK < 2){
                if(intent!!.getStringExtra("skim") != null){
                    info =intent!!.getStringExtra("skim")!!
                }
            }
            viewModel.getDataSkim(info,LEVEL_CHECK)
        }

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(currentItem: Int) {
                binding.tabs.selectTab(binding.tabs.getTabAt(currentItem))
            }
        })
    }

}