package com.razitulikhlas.banknagari.ui.calculator

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.data.model.TableInstallment
import com.razitulikhlas.core.util.Constant.rupiah
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.ceil
import com.itextpdf.text.Image
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList


class TableCalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val list = ArrayList<TableInstallment>()
    private var pladfond = 0.0
    private var timeLoan = 0
    private var interest = 0.0
    private val context = application.applicationContext
    fun flowerFlat(pf:Double, flower: Double, timeLoan:Int, type:String):ArrayList<TableInstallment>{
        list.clear()
        pladfond =pf
        var sisapinjaman = pladfond
        this.timeLoan =timeLoan
        interest = flower
        var installmentPrimary = ceil(pf/timeLoan)
        var bankInterest = 0.0
        var totalFlower = 0.0

        bankInterest = if(type == "TAHUN")
            ((flower/12)/100)*pf
        else
            (flower/100)*pf

        bankInterest = ceil(bankInterest)

        val total = installmentPrimary+bankInterest

        for (i in 1..timeLoan){
            if(i == timeLoan){
                installmentPrimary = pf - ((timeLoan - 1) * installmentPrimary)
            }
            totalFlower+=bankInterest

            sisapinjaman -= installmentPrimary
            val tableInstallment = TableInstallment(i,installmentPrimary,bankInterest,total,sisapinjaman)
            list.add(tableInstallment)
        }
        return list
    }

    fun flowerAnu(pf:Double, flower: Double, timeLoan:Int):ArrayList<TableInstallment>{
        list.clear()
        pladfond =pf
        this.timeLoan =timeLoan
        interest = flower
        var sisapinjaman = pladfond
        val installmentPrimary = ceil(pf/timeLoan)
        var bankInterest = 0.0
        var totalFlower = 0.0

        for (i in 1..timeLoan){
            bankInterest = ((flower/12)/100) * sisapinjaman
            bankInterest = ceil(bankInterest)
            totalFlower+=bankInterest
            val total = installmentPrimary+bankInterest
            sisapinjaman -= installmentPrimary
            val tableInstallment = TableInstallment(i,installmentPrimary,bankInterest,total,sisapinjaman)
            list.add(tableInstallment)
        }
        return list
    }

  @SuppressLint("SuspiciousIndentation")
  fun exportToPDF() :File {
        // Create PDF document object
        val document = Document(PageSize.A4, 30f, 30f, 30f, 30f)

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        if (!dir.canWrite()) {
            // Handle error, such as showing a dialog to inform the user
        }
        // Set PDF file path

        val file = File(dir, "myFile.pdf")
        val outputStream = FileOutputStream(file)


        // Create PDF writer instance
      val writer =PdfWriter.getInstance(document, outputStream)
        // Open document
        document.open()

        // Tambahkan judul rata tengah pada dokumen
        val judul = "Tabel Angsuran Kredit dengan plafond ${rupiah(pladfond)} dengan jangka waktu selama $timeLoan bulan dengan bunga perbulan sebesar ${String.format("%.2f",(interest/12).round(2))}%"
        val fontJudul = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
        val paragraphJudul = Paragraph(judul, fontJudul)
        paragraphJudul.alignment = Element.ALIGN_CENTER
        document.add(paragraphJudul)
        document.add(Paragraph(Phrase(Chunk.NEWLINE)))
        document.add(Paragraph(Phrase(Chunk.NEWLINE)))

        // Create table with 3 columns
        val table = PdfPTable(5)
        table.setWidths(floatArrayOf(0.3f, 1f, 1f,1f,1f)) // Set column widths


        val header1 = PdfPCell(Phrase("No", Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)))
        header1.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(header1)
        val header2 = PdfPCell(Phrase("Angsuran Pokok", Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)))
        header2.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(header2)
        val header3 = PdfPCell(Phrase("Bunga", Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)))
        header3.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(header3)
        val header4 = PdfPCell(Phrase("Jumlah Pinjaman", Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)))
        header4.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(header4)
        val header5 = PdfPCell(Phrase("Sisa Pinjaman", Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)))
        header5.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(header5)

        for(i in 0 until list.size){
            val cell1 = PdfPCell(Paragraph("${i + 1}"))
            cell1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell1)
            val cell2 = PdfPCell(Paragraph(rupiah(list[i].installmentPrimary)))
            cell2.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell2)
            val cell3 = PdfPCell(Paragraph(rupiah(list[i].bankInterest)))
            cell3.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell3)
            val cell4 = PdfPCell(Paragraph(rupiah(list[i].installmentAmount)))
            cell4.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell4)
            val cell5 = PdfPCell(Paragraph(rupiah(list[i].remainingLoan)))
            cell5.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell5)
        }

//      val watermarkBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo) // membuka file image watermark dari drawable
//      val stream = ByteArrayOutputStream()
//      watermarkBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//      val byteArray = stream.toByteArray()
//      val watermarkImage = Image.getInstance(byteArray) // membuat objek Image dari bitmap
//
////       PdfWriter.getInstance(document, FileOutputStream(outputFile))
//      writer.setPageEvent(object : PdfPageEventHelper() {
//          override fun onEndPage(writer: PdfWriter, document: Document) {
//              val contentByte = writer.directContent
//              contentByte.saveState()
//              val x = (document.pageSize.width - watermarkImage.scaledWidth) / 2 // menentukan posisi x
//              val y = (document.pageSize.height - watermarkImage.scaledHeight) / 2 // menentukan posisi y
//              contentByte.addImage(watermarkImage, watermarkImage.scaledWidth, 0f, 0f, watermarkImage.scaledHeight, x, y)
//              contentByte.restoreState()
//          }
//      })







        // Add table to PDF document

        // Close document
        document.add(table)
        document.close()
        outputStream.close()


        Log.e("TAG", "exportToPDF: ", )
      return file
    }




    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

}