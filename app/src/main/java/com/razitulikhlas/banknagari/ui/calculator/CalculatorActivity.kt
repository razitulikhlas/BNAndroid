package com.razitulikhlas.banknagari.ui.calculator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.data.model.TableInstallment
import com.razitulikhlas.banknagari.databinding.ActivityCalculatorBinding
import com.razitulikhlas.core.util.Constant.rupiah
import com.razitulikhlas.core.util.format.RupiahFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class CalculatorActivity : AppCompatActivity() {
    lateinit var binding : ActivityCalculatorBinding
    private val viewModel: TableCalculatorViewModel by viewModel()
    var data = ArrayList<TableInstallment>()
    var pf = 0.0
    var bl = 0
    var bg =0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        val types = arrayOf("Flat","Sliding")
        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            types
        )
        with(binding){
            type.setAdapter(adapter)
            edPlatfond.addTextChangedListener(RupiahFormat(edPlatfond))


            btnProses.setOnClickListener {
                val pfs = edPlatfond.text.toString()
                pf = pfs.replace("Rp ","").replace(".","").toDouble()
                bl = edMonth.text.toString().toInt()
                bg = edBunga.text.toString().toDouble()
                data = if(type.text.toString() == "Flat"){
                    viewModel.flowerFlat(pf,bg,bl,"TAHUN")
                }else{
                    viewModel.flowerAnu(pf,bg,bl)
                }


                tvPladfond.text = pfs
                tvAsPokok.text = rupiah(data[0].installmentPrimary)
                tvBunga.text = rupiah(data[0].bankInterest)
                tvTotalAngsuran.text = rupiah(data[0].installmentAmount)
            }

            btnShare.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this@CalculatorActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this@CalculatorActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@CalculatorActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                } else {
//                    sharePdf()
                   val file = viewModel.exportToPDF()
                    if(file.exists()){
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                        intent.type = "application/pdf"
                        startActivity(intent)
                    }

                }
            }

            btnTblAn.setOnClickListener {
                if(data.isNotEmpty()){
                    val intent = Intent(this@CalculatorActivity,TableCalculatorActivity::class.java)
                    intent.putExtra("PF",pf)
                    intent.putExtra("BL",bl)
                    intent.putExtra("BG",bg)
                    intent.putExtra("TYPE",binding.type.text.toString())
                    startActivity(intent)
                }else{
                    Toast.makeText(this@CalculatorActivity,"Silahkan  hitung dulu",Toast.LENGTH_SHORT).show()
                }

            }


        }
    }

    fun sharePdf(){
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(dir, "myFile.pdf")


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.exportToPDF()
        }
    }

    fun tblAng(){
        val tableInstallments = ArrayList<TableInstallment>()
        val bln = 36
        val installmentPrimary = 466666
        val bankInterest = 1000000
        val installmentAmount = 566666
        val pld = 200000000

        for(i in 1..bln){
            val remainingLoan : Double = (pld-installmentAmount).toDouble()
            val data = TableInstallment(i,installmentPrimary.toDouble(),bankInterest.toDouble(),installmentAmount.toDouble(), remainingLoan)
//            tableInstallments.add()
        }
    }

    fun sharePDFViaWhatsApp(file: File) {
        val authority = "com.razitulikhlas.banknagari.fileprovider" // Ganti dengan authority dari FileProvider di aplikasi Anda
        val contentUri = FileProvider.getUriForFile(this, authority, file)
        val shareIntent = Intent(Intent.ACTION_SEND)

        val file = File(filesDir, "my_file.pdf") // mengambil file yang disimpan di direktori files internal aplikasi
        val uri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", file) // membuat URI menggunakan FileProvider

        shareIntent.type = "application/pdf"
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.setPackage("com.whatsapp")
        startActivity(Intent.createChooser(shareIntent, "Bagikan file PDF ke:"))
    }



}