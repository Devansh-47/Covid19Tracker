package com.example.covid19

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var listofallstates=ArrayList<responseclass>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
        val e=sharedPref.edit()
        e.putLong("time", System.currentTimeMillis())
        e.apply()
        u_time.text="LAST UPDATE seconds AGO"

        refresh.setOnRefreshListener {
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            val e=sharedPref.edit()
            e.putLong("time", System.currentTimeMillis())
            e.apply()
            u_time.text="LAST UPDATE seconds AGO"
            refresh.isRefreshing=false
            try{
               //fetchresult()
            }catch (e:Exception){
                Log.d("WQW","="+e.toString())
            }

        }

        refresh.setOnChildScrollUpCallback(object : SwipeRefreshLayout.OnChildScrollUpCallback {
            override fun canChildScrollUp(parent: SwipeRefreshLayout, child: View?): Boolean {
                if (rv != null) {
                    return rv.canScrollVertically(-1)
                }
                return false
            }
        })


        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                val sharedPref = this@MainActivity?.getPreferences(Context.MODE_PRIVATE)
                val timeinmill=sharedPref.getLong("time",System.currentTimeMillis())
                val diff=(System.currentTimeMillis()-timeinmill)/1000
                u_time.text="LAST UPDATE ${diff} seconds AGO"
                mainHandler.postDelayed(this, 10000)
            }
        })

        fetchresult()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchresult(){
        GlobalScope.launch {

            val response: Response =  withContext(Dispatchers.IO){Client.api.execute()}
            if(response.isSuccessful){
                try {
                    //val json=JsonParser().parse(response.body?.string())
                    val arr=JSONArray(response.body?.string())
                    for (i in 0 until arr.length()){
                        val con=arr.getJSONObject(i).getString("Country")
                        val p=arr.getJSONObject(i).getString("Province")
                        val c=arr.getJSONObject(i).getInt("Confirmed")
                        val death=arr.getJSONObject(i).getInt("Deaths")
                        val r=arr.getJSONObject(i).getInt("Recovered")
                        val a=arr.getJSONObject(i).getInt("Active")
                        val d=arr.getJSONObject(i).getString("Date")
                        listofallstates.add(responseclass(con,p,c,death,r,a,d))
                    }
                    Log.d("ASD","list sizeee=="+listofallstates.size)
                    withContext(Dispatchers.Main){
                        rv.addHeaderView(LayoutInflater.from(this@MainActivity).inflate(R.layout.layout_simple,asa,false))
                        rv.adapter=adapter(this@MainActivity,listofallstates)
                    }

                }catch (e:Exception){
                    Log.d("ASD","=="+e.toString())
                }

            }


            val response1: Response =  withContext(Dispatchers.IO){Client.api1.execute()}
            if(response1.isSuccessful){
                try {
                    //val json=JsonParser().parse(response.body?.string())
                    val obj=JSONObject(response1.body?.string())
                    val arr=obj.getJSONArray("Countries")
                    for (i in 0 until arr.length()){
                        if(arr.getJSONObject(i).getString("Country").equals("India")){
                            withContext(Dispatchers.Main){
                                val c_cases=arr.getJSONObject(i).getInt("TotalConfirmed")
                                val a_cases=arr.getJSONObject(i).getInt("NewConfirmed")
                                val r_cases=arr.getJSONObject(i).getInt("TotalRecovered")
                                val d_cases=arr.getJSONObject(i).getInt("TotalDeaths")
                                Log.d("ASD","c==l=l= ${arr.getJSONObject(i).getString("ID")} ${c_cases}  ${a_cases} ${r_cases} ${d_cases}")


                                GlobalScope.launch(Dispatchers.Main) {
                                    try {
                                        var step=c_cases/1000
                                        for (k in 0 until c_cases step step) {
                                            confirmedcases.text=""+k
                                            delay(3)
                                        }
                                    }catch (e:Exception){
                                        Log.d("AAA","a="+e.toString())
                                    }

                                }
                                GlobalScope.launch(Dispatchers.Main) {
                                    try {
                                        var step=a_cases/1000
                                        for (k in 0 until a_cases step  step ) {
                                            active_cases.text=""+k
                                            delay(3)
                                        }
                                    }catch (e:Exception){
                                        Log.d("AAA","a="+e.toString())
                                    }

                                }

                                GlobalScope.launch(Dispatchers.Main) {
                                    try {
                                        var step=d_cases/1000
                                        for (k in 0 until d_cases step step) {
                                            deceased_cases.text=""+k
                                            delay(3)
                                        }
                                    }catch (e:Exception){
                                        Log.d("AAA","a="+e.toString())
                                    }

                                }


                            }
                        }
                    }
                    Log.d("ASD","list sizeee=="+listofallstates.size)
                }catch (e:Exception){
                    Log.d("ASD","=="+e.toString())
                }

            }

        }

    }
}