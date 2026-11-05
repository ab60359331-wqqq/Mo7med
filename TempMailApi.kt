package com.example.tempmail // تأكد إن اسم الباكدج عندك مطابق للمشروع

import retrofit2.http.GET

interface TempMailApi {
    @GET("api/v1/index.php?action=genRandomGmail")
    suspend fun getRandomEmail(): List<String>
}