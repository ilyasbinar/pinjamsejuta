package com.example.pinjamsejuta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinjamsejuta.R
import androidx.core.graphics.toColorInt
import com.example.pinjamsejuta.model.loan.Plafond

class PlafondAdapter(
    private var items: List<Plafond>,
    private val onAjukanClicked: (Plafond) -> Unit
) : RecyclerView.Adapter<PlafondAdapter.PlafondViewHolder>() {

    inner class PlafondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val detail: TextView = itemView.findViewById(R.id.tvDetail)
        val btnAjukan: Button = itemView.findViewById(R.id.btnAjukan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlafondViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plafond, parent, false)
        return PlafondViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlafondViewHolder, position: Int) {
        val plafon = items[position]
        holder.name.text = plafon.name
        holder.detail.text = buildString {
            append("Hingga Rp ${plafon.maxAmount.toInt()}\n")
            append("Bunga: ${(plafon.interestRate * 100)}%\n")
            append("Tenor: ${plafon.minTenor}–${plafon.maxTenor} bulan")
        }

        holder.btnAjukan.setOnClickListener { onAjukanClicked(plafon) }

        when (plafon.name.lowercase()) {
            "gold" -> holder.itemView.setBackgroundColor("#FFF9C4".toColorInt()) // gold
            "silver" -> holder.itemView.setBackgroundColor("#ECEFF1".toColorInt()) // silver
            "bronze" -> holder.itemView.setBackgroundColor("#FFE0B2".toColorInt()) // bronze
            "platinum" -> holder.itemView.setBackgroundColor("#E1F5FE".toColorInt()) // platinum
            else -> holder.itemView.setBackgroundColor("#FFFFFF".toColorInt()) // default
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Plafond>) {
        items = newItems
        notifyDataSetChanged() // Bisa diganti DiffUtil untuk performa lebih baik
    }
}