package com.example.charmrides.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.charmrides.EntityRes.BusItem
import com.example.charmrides.R

class BusAdapter(
    private val busCardClicked: (BusItem) -> Unit,
    contexts: Context
): RecyclerView.Adapter<BusAdapterViewHolder>() {
    val conts = contexts
    private val busList = ArrayList<BusItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.bus_item,parent,false)
        return BusAdapterViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    override fun onBindViewHolder(holder: BusAdapterViewHolder, position: Int) {
        holder.bind(busList[position],busCardClicked, conts)
    }
    fun setList(busItem: List<BusItem>){
        busList.clear()
        busList.addAll(busItem)
        notifyDataSetChanged()
    }
}

class BusAdapterViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
    fun bind(busItem: BusItem,busCardClicked:(BusItem)->Unit,context: Context){
        val tvPriceBus = view.findViewById<TextView>(R.id.tvPriceBus)
        val tvBusName = view.findViewById<TextView>(R.id.tvBusName)
        val cvBusItemCard = view.findViewById<CardView>(R.id.cvBusItemCard)

        tvPriceBus.text = "Rs.${busItem.price}"
        tvBusName.text = busItem.name.capitalize()

        cvBusItemCard.setOnClickListener {
            busCardClicked(busItem)
        }
    }

}