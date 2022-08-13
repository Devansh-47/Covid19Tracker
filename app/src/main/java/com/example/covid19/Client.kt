package com.example.covid19

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.time.temporal.ChronoUnit


object Client {
    var client = OkHttpClient()


    @RequiresApi(Build.VERSION_CODES.O)
    val now: Instant = Instant.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val yesterday: Instant = now.minus(1, ChronoUnit.DAYS)

    var request: Request = Request.Builder()
        .url("https://api.covid19api.com/live/country/india/status/confirmed/date/$yesterday")
        .build()

    val api= client.newCall(request)

    var request1: Request = Request.Builder()
        .url("https://api.covid19api.com/summary")
        .build()

    val api1= client.newCall(request1)
}