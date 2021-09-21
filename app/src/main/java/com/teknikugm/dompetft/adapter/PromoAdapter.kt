package com.teknikugm.dompetft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.model.ResponsePromo
import kotlinx.android.synthetic.main.activity_promo_adapter.view.*

class PromoAdapter(private var data : List<ResponsePromo?>?, private var context : Context, private var onclick : (ResponsePromo?)->Unit) : RecyclerView.Adapter<PromoAdapter.ViewHolder>() {

    class ViewHolder (item : View) : RecyclerView.ViewHolder(item) {
        val kodepromo = item.kode_promo
        val persentasepromo = item.persentase_promo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
        val l = view.inflate(R.layout.activity_promo_adapter, null)
        return ViewHolder(l)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = data?.get(position)
        holder.kodepromo.text = dataItem?.kodePromo
        holder.persentasepromo.text = dataItem?.persentasePromo.toString()
        holder.itemView.setOnClickListener() { onclick(dataItem) }
    }

    override fun getItemCount(): Int {
        return data?.size ?:0
    }

}