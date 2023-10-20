package com.example.charmrides.Fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.charmrides.Adapter.PayAdapter
import com.example.charmrides.ApiService.BusServices
import com.example.charmrides.EntityRes.BusRes
import com.example.charmrides.EntityRes.PayRes
import com.example.charmrides.EntityRes.PaymentItem
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R
import com.example.charmrides.RetrofitService.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookingFragment : Fragment() {
    private lateinit var rvReportFrag: RecyclerView
    private lateinit var payAdapter: PayAdapter
    private lateinit var out: UserRecord


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_booking, container, false)

        out = arguments?.getSerializable("user", UserRecord::class.java)!!
        initRecycler(view)
        return view
    }
    private fun initRecycler(view:View){
        rvReportFrag = view.findViewById(R.id.rvReservationFrag)
        rvReportFrag.layoutManager= LinearLayoutManager(requireActivity())
        payAdapter = PayAdapter ({
                payItem: PaymentItem -> payCardClicked(payItem)
        },requireContext() )
        rvReportFrag.adapter = payAdapter
        fetchDetails()
    }

    private fun fetchDetails() {
        val emailToFilter: String = out.record.email
        val filterValue = "(userEmail=\"$emailToFilter\")"

        val retrofitService= RetrofitService()
        val getList =retrofitService.getRetrofit().create(BusServices::class.java)
        val call: Call<PayRes> = getList.getAllPay(filterValue)

        call.enqueue(object : Callback<PayRes> {
            override fun onResponse(call: Call<PayRes>, response: Response<PayRes>) {
                if(response.isSuccessful){
                    if (response.body()!=null){
                        val mealRes = response.body()
                        val mealItem = mealRes?.items
                        payAdapter.setList(mealItem!!)
                    }
                }else{
                    Toast.makeText(requireContext(),"Invalid response", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PayRes>, t: Throwable) {
                Toast.makeText(requireContext(),"Server Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun payCardClicked(payItem: PaymentItem) {
        Toast.makeText(requireContext(),payItem.busName.capitalize(),Toast.LENGTH_SHORT).show()
    }

}