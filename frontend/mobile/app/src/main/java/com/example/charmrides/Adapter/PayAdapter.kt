package com.example.charmrides.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.charmrides.EntityRes.PaymentItem
import com.example.charmrides.R

class PayAdapter(
    private val payCardClicked: (PaymentItem) -> Unit,
    contexts: Context
): RecyclerView.Adapter<PayAdapterViewHolder>() {
    val conts = contexts
    private val payList = ArrayList<PaymentItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.transaction_item,parent,false)
        return PayAdapterViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return payList.size
    }

    override fun onBindViewHolder(holder: PayAdapterViewHolder, position: Int) {
        holder.bind(payList[position],payCardClicked, conts)
    }
    fun setList(payItem: List<PaymentItem>){
        payList.clear()
        payList.addAll(payItem)
        notifyDataSetChanged()
    }
}

class PayAdapterViewHolder(private val view: View):RecyclerView.ViewHolder(view) {
    fun bind(payItem: PaymentItem,payCardClicked:(PaymentItem)->Unit,context: Context){
        val tvPayDoneItemCard = view.findViewById<TextView>(R.id.tvPayDoneItemCard)
        val cvPaymentItemCard = view.findViewById<CardView>(R.id.cvWrkOutCard)


        tvPayDoneItemCard.text = "Rs.${payItem.amount}.00"

        cvPaymentItemCard.setOnClickListener {
            payCardClicked(payItem)
        }
    }

}