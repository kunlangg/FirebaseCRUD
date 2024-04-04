package com.example.datateman

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.LauncherActivity.ListItem
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
class RecyclerViewAdapter (private val dataTeman: ArrayList<data_teman>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val context: Context

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val Nama: TextView
            val Alamat: TextView
            val NoHP: TextView
            val ListItem: LinearLayout

            init {
                Nama = itemView.findViewById(R.id.namax)
                Alamat = itemView.findViewById(R.id.alamatx)
                NoHP = itemView.findViewById(R.id.no_hpx)
                ListItem = itemView.findViewById(R.id.list_item)
            }
        }
    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V:View = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.view_design, parent, false)
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val Nama: String? = dataTeman.get(position).nama
        val Alamat: String? = dataTeman.get(position).alamat
        val NoHP: String? = dataTeman.get(position).no_hp

        holder.Nama.text = "Nama: $Nama"
        holder.Alamat.text = "Alamat: $Alamat"
        holder.NoHP.text = "NoHP: $NoHP"
        holder.ListItem.setOnLongClickListener { view ->
            val action = arrayOf("Update", "Delete")
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setItems(action) { dialog, i ->
                when (i) {
                    0 -> {
                        val bundle = Bundle().apply {
                            putString("dataNama", dataTeman[position].nama)
                            putString("dataAlamat", dataTeman[position].alamat)
                            putString("DataNoHP", dataTeman[position].no_hp)
                            putString("getPrimaryKey", dataTeman[position].key)
                        }
                        val intent = Intent(view.context, UpdateData::class.java).apply {
                            putExtras(bundle)
                        }
                        context.startActivity(intent)
                    }
                    1 -> {
                        deleteData(position)
                    }
                }
            }
            alertDialogBuilder.create().show()
            true
        }
    }

    override fun getItemCount(): Int {
        return dataTeman.size
    }

    private fun deleteData(position: Int) {
        val database = FirebaseDatabase.getInstance().reference
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val key = dataTeman[position].key

        if (userID != null && key != null) {
            database.child("Admin").child(userID).child("DataTeman").child(key)
                .removeValue()

                }
            }

}

