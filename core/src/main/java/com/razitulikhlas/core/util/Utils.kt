package com.razitulikhlas.core.util


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.util.Log.e
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iceteck.silicompressorr.FileUtils
import com.iceteck.silicompressorr.SiliCompressor
import com.razitulikhlas.core.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Math.log10
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

fun createPartFromString(descriptionString: String): RequestBody? {
    return descriptionString.toRequestBody(MultipartBody.FORM)
}

fun prepareFilePart(
    name: String,
    file: Uri?,
    context: Context
): MultipartBody.Part? {
    e("TAG", "prepareFilePart: start")
    if(file != null){
        var originalFile = File(file?.path!!)
//    val originalFile = File(Objects.requireNonNull(PathUtil.getPath(context, file!!)));

        val con = Uri.fromFile(originalFile)
        e("TAG", "size sebelum di compress: ${getReadableFileSize(originalFile.length())}")
        if (originalFile.length() > 2000000) {
            originalFile = File(
                SiliCompressor.with(context)
                    .compress(FileUtils.getPath(context, con), File(context.cacheDir, "temp"))
            )
        }
//    e("TAG", "size setelah di compress: ${getReadableFileSize(originalFile.length())}")
        e("tag",originalFile.toString())
        val filePart = originalFile
            .asRequestBody(
                context.contentResolver.getType(file)?.toMediaTypeOrNull()
            )
        return MultipartBody.Part.createFormData(name, originalFile.name, filePart)

    }else{
        return null
    }

}

private fun getReadableFileSize(size: Long): String {
    if (size <= 0) {
        return "0"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}



fun getCurrentDate():String{
    val sdf = SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss",Locale.getDefault())
    return sdf.format(Date())
}

fun Activity.checkFocus(layout: TextInputLayout, editText: TextInputEditText) {
    editText.setOnFocusChangeListener { _, b ->
        if (b) {
            try {
                ContextCompat.getColorStateList(this, R.color.selector)?.let {
                    layout.setBoxStrokeColorStateList(
                        it
                    )
                }
                editText.setHintTextColor(ContextCompat.getColor(this, R.color.blue))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.blue))
        } else {
            if (editText.text.toString().isNotEmpty()) {
                try {
                    ContextCompat.getColorStateList(this, R.color.selector)?.let {
                        layout.setBoxStrokeColorStateList(
                            it
                        )
                    }
                    editText.setHintTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.blue
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    editText.setHintTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.hint_color
                        )
                    )
                    ContextCompat.getColorStateList(this, R.color.noselector)?.let {
                        layout.setBoxStrokeColorStateList(
                            it
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun Fragment.hasPermissions(permissions: Array<String>): Boolean =
    permissions.all {
        ActivityCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

fun Fragment.hasPermission(permissions: String): Boolean =
    ActivityCompat.checkSelfPermission(
        requireContext(),
        permissions
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.checkPermission(permissions: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(
        requireActivity(),
        permissions
    )


fun showToastLong(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun showToastShort(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()!!
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    return dir!!.delete()
}

fun Activity.clearApplicationData() {
    val cache = File(cacheDir, "/temp/")
    Log.e("TAG", "clearApplicationData: ${cache}", )
//        val appDir = File(cache.parent)
    if (cache.exists()) {
        val children = cache.list()!!
        for (s in children) {
            Log.e("TAG", "clearApplicationData: ${s}", )
            if (s != "lib") {
                deleteDir(File(cache, s))
                Log.i(
                    "TAG",
                    "**************** File $cache $s DELETED *******************"
                )
            }
        }
    }
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timeZone: TimeZone = TimeZone.getTimeZone("Asia/Jakarta")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount).substringBefore(",")
}

