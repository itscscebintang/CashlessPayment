package com.teknikugm.dompetft.pembayaran

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teknikugm.dompetft.R
import kotlinx.android.synthetic.main.activity_promo_adapter.view.*
import kotlinx.android.synthetic.main.activity_promo_adapter.view.*

class PromoAdapter(private var data : List<DataItem?>?, private var context : Context, private var onclick : (DataItem?)->Unit) : RecyclerView.Adapter<PromoAdapter.ViewHolder>() {

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {

        val kodepromo = item.kode_promo
        val minbelanja = item.min_belanja
        val persentasepromo = item.persentase_promo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoAdapter.ViewHolder {
        val view = LayoutInflater.from(context)
        val l = view.inflate(R.layout.activity_promo_adapter, null)
        return ViewHolder(l)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = data?.get(position)
        val b = 1
        holder.kodepromo.text = dataItem?.kodePromo
        holder.minbelanja.text = dataItem?.minBelanja
        holder.persentasepromo.text = dataItem?.persentasePromo
        holder.itemView.setOnClickListener() { onclick(dataItem) }
    }

    override fun getItemCount(): Int {
        return data?.size ?:0
    }

}