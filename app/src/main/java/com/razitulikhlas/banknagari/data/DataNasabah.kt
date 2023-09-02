package com.razitulikhlas.banknagari.data

import com.razitulikhlas.banknagari.data.model.Nasabah

object DataNasabah {
    fun generateDummyNasabah(): List<Nasabah> {
        val nasabah = ArrayList<Nasabah>()
        nasabah.add(Nasabah("Rizkhan Hadi","Rp 250 juta","Kol 1"))
        nasabah.add(Nasabah("Rizki Aditya","Rp 600 juta","Kol 2"))
        nasabah.add(Nasabah("Chairul Tanjung","Rp 250 juta","Kol 1"))
        return nasabah
    }

    fun generateDummyNasabahPotensial(): List<Nasabah> {
        val nasabah = ArrayList<Nasabah>()
        nasabah.add(Nasabah("Rizkhan Hadi","Rp 250 juta","Kol 1"))
        nasabah.add(Nasabah("Rizki Aditya","Rp 550 juta","Kol 1"))
        nasabah.add(Nasabah("Chairul Tanjung","Rp 250 juta","Kol 1"))
        return nasabah
    }
}