package com.example.charmrides.Fragment

import android.content.Intent
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
import com.example.charmrides.Activity.BusDetails
import com.example.charmrides.Adapter.BusAdapter
import com.example.charmrides.ApiService.BusServices
import com.example.charmrides.EntityRes.BusItem
import com.example.charmrides.EntityRes.BusRes
import com.example.charmrides.EntityRes.UserRecord
import com.example.charmrides.R
import com.example.charmrides.RetrofitService.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusFragment : Fragment() {
    private lateinit var rvReportFrag: RecyclerView
    private lateinit var busAdapter: BusAdapter
    private lateinit var out: UserRecord

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_bus, container, false)

        out = arguments?.getSerializable("user", UserRecord::class.java)!!

        initRecycler(view)

        return view
    }

    private fun initRecycler(view:View){
        rvReportFrag = view.findViewById(R.id.rvBusFrag)
        rvReportFrag.layoutManager= LinearLayoutManager(requireActivity())
        busAdapter = BusAdapter ({
                busItem: BusItem -> busCardClicked(busItem)
        },requireContext() )
        rvReportFrag.adapter = busAdapter
        fetchDetails()
    }

    private fun busCardClicked(busItem: BusItem) {
        val bundle = Bundle()
        bundle.putSerializable("user", out)
        bundle.putSerializable("bus", busItem)

        val intent = Intent(requireActivity(), BusDetails::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    private fun fetchDetails() {
        val retrofitService= RetrofitService()
        val getList =retrofitService.getRetrofit().create(BusServices::class.java)
        val call: Call<BusRes> = getList.getAlBuses()

        call.enqueue(object : Callback<BusRes> {
            override fun onResponse(call: Call<BusRes>, response: Response<BusRes>) {
                if(response.isSuccessful){
                    if (response.body()!=null){
                        val mealRes = response.body()
                        val mealItem = mealRes?.items
                        busAdapter.setList(mealItem!!)
                    }
                }else{
                    Toast.makeText(requireContext(),"Invalid response", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BusRes>, t: Throwable) {
                Toast.makeText(requireContext(),"Server Error", Toast.LENGTH_SHORT).show()
            }
        })
    }


}