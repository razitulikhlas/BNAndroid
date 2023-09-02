package com.razitulikhlas.banknagari.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.razitulikhlas.banknagari.utils.AppConstant.LOCATION_REQUEST_CODE

class AppPermission {

    fun isLocationOk(context:Context):Boolean{
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }else{
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

    }

    fun showPermissionRequestPermissionRationale(activity: Activity):Boolean{
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)
        }else{
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    fun requestPermissionLocation(activity: Activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Log.e("TAG", "requestPermissionLocation1: ", )
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), LOCATION_REQUEST_CODE
            )
        } else {
            Log.e("TAG", "requestPermissionLocation2: ", )
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_REQUEST_CODE
            )
        }
    }

    fun showDialogShowRequestPermissionRationale(activity: Activity){
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("aplikasi ini membutuhkan akses dari lokasi anda")
            .setTitle("Permohonan akses lokasi")
            .setCancelable(false)
            .setPositiveButton("Oke") { dialogInterface, i ->
                requestPermissionLocation(activity)
                dialogInterface.dismiss()
            }
            .setNegativeButton("cancel"){dialog,i->dialog.dismiss()}
            .show()
    }

    fun showDialogShowRequestPermissionLocation(activity: Activity){
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("fitur ini tidak tersedia jika anda tidak memberikan izin untuk mengakses aplikasi anda," +
                "Tolong izinkan akses lokasi anda saat ini")
            .setTitle("Permohonan akses lokasi")
            .setCancelable(false)
            .setPositiveButton("Settings") { dialogInterface, i ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",activity.packageName,null)
                intent.data = uri
                activity.startActivity(intent)
                dialogInterface.dismiss()
            }
            .setNegativeButton("cancel"){dialog,i->dialog.dismiss()}
            .show()
    }


}