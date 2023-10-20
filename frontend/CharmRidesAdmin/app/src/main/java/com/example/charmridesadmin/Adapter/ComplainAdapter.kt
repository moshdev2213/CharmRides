package com.example.charmridesadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.charmridesadmin.EntityRes.ComplainItem
import com.example.charmridesadmin.R

class ComplainAdapter(
    private val complainCardClicked: (ComplainItem) -> Unit,
    contexts: Context
): RecyclerView.Adapter<ComplainAdapterViewHolder>() {
    val conts = contexts
    private val complainList = ArrayList<ComplainItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.complaint_item,parent,false)
        return ComplainAdapterViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return complainList.size
    }

    override fun onBindViewHolder(holder: ComplainAdapterViewHolder, position: Int) {
        holder.bind(complainList[position],complainCardClicked, conts)
    }
    fun setList(complainItem: List<ComplainItem>){
        complainList.clear()
        complainList.addAll(complainItem)
        notifyDataSetChanged()
    }
}

class ComplainAdapterViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
    fun bind(complainItem: ComplainItem,complainCardClicked:(ComplainItem)->Unit,context: Context){
        val tvComplainDesc = view.findViewById<TextView>(R.id.tvMainNameDis)
        val tvComplainDate = view.findViewById<TextView>(R.id.tvPayDate)
        val tvComplainName = view.findViewById<TextView>(R.id.tvPayTime)
        val cvComplainItemCard = view.findViewById<CardView>(R.id.cvMainPayCardReservation)

        tvComplainDesc.text = complainItem.userEmail
        tvComplainDate.text = complainItem.userName
        tvComplainName.text = complainItem.busName

        cvComplainItemCard.setOnClickListener {
            complainCardClicked(complainItem)
        }
    }

}