package com.example.charmridesadmin.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.charmridesadmin.Adapter.ComplainAdapter
import com.example.charmridesadmin.ApiService.ComplainService
import com.example.charmridesadmin.EntityRes.ComplainFormRes
import com.example.charmridesadmin.EntityRes.ComplainItem
import com.example.charmridesadmin.R
import com.example.charmridesadmin.RetrofitService.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportFragment : Fragment() {
    private lateinit var rvReportFrag:RecyclerView
    private lateinit var complainAdapter: ComplainAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_report, container, false)

        rvReportFrag = view.findViewById(R.id.rvReportFrag)
        initRecycler(view)
        return view
    }
    private fun initRecycler(view:View){
        rvReportFrag = view.findViewById(R.id.rvReportFrag)
        rvReportFrag.layoutManager= LinearLayoutManager(requireActivity())
        complainAdapter = ComplainAdapter ({
                complainItem: ComplainItem -> complainCardClicked(complainItem)
        },requireContext() )
        rvReportFrag.adapter = complainAdapter
        fetchDetails()
    }

    private fun fetchDetails() {
        val retrofitService= RetrofitService()
        val getList =retrofitService.getRetrofit().create(ComplainService::class.java)
        val call: Call<ComplainFormRes> = getList.getAllComplains()

        call.enqueue(object : Callback<ComplainFormRes> {
            override fun onResponse(call: Call<ComplainFormRes>, response: Response<ComplainFormRes>) {
                if(response.isSuccessful){
                    if (response.body()!=null){
                        val mealRes = response.body()
                        val mealItem = mealRes?.items
                        complainAdapter.setList(mealItem!!)
                    }
                }else{
                    Toast.makeText(requireContext(),"Invalid response", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ComplainFormRes>, t: Throwable) {
                Toast.makeText(requireContext(),"Server Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun complainCardClicked(complainItem: ComplainItem) {
        Toast.makeText(requireContext(),complainItem.id,Toast.LENGTH_SHORT).show()
    }

}