package com.example.datateman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datateman.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    //deklarasikan variable
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHP: String? = null
    private lateinit var binding: ActivityUpdateDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Update Data"

        //mendapatkan instance autentikasi dan referensi dari database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data //memanggil method data
        binding.update.setOnClickListener {
            //mendapatkan data teman yang akan dicek
            cekNama = binding.newNama.text.toString()
            cekAlamat = binding.newAlamat.text.toString()
            cekNoHP = binding.newNohp.text.toString()

            //mengecek agar tidak ada data yang kosong
            if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHP!!)) {
                Toast.makeText(this@UpdateData, "Data Tidak Boleh Kosong",
                    Toast.LENGTH_SHORT).show()
            } else {
//menjalankan update data
                val setTeman = data_teman()
                setTeman.nama = binding.newNama.text.toString()
                setTeman.alamat = binding.newAlamat.text.toString()
                setTeman.no_hp = binding.newNohp.text.toString()
                updateTeman(setTeman)
            }
        }
    }

    //mengecek apakah ada data kosong, sebelum update
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    //menampilkan data yang akan diupdate
    private val data: Unit
        private get() {
            //menampilkan data dari item yang yang sudah dipilih
            val getNama = intent.getStringExtra("dataNama")
            val getAlamat = intent.getStringExtra("dataAlamat")
            val getNoHP = intent.getStringExtra("dataNoHP")
            binding.newNama.setText(getNama)
            binding.newAlamat.setText(getAlamat)
            binding.newNohp.setText(getNoHP)
        }

    //proses update data yang ditentukan
    private fun updateTeman(teman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.getStringExtra("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataTeman")
            .child(getKey!!)
            .setValue(teman)
            .addOnSuccessListener {
                binding.newNama.setText("")
                binding.newAlamat.setText("")
                binding.newNohp.setText("")
                Toast.makeText(
                    this@UpdateData, "Data Berhasil Diubah",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
    }
}